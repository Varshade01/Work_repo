package com.maat.cha.feature.splash.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Message
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.maat.cha.feature.splash.utils.SplashConstants

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComposable(
    url: String,
    onBackPressed: () -> Unit,
    onExternalNavigation: () -> Unit = {},
    onWebViewError: () -> Unit = {}
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    var showNoInternet by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var navigationStack by remember { mutableStateOf(listOf<String>()) }

    // Handle back button with double-tap exit
    BackHandler {
        if (showNoInternet) {
            // If showing no internet screen, go back to main app
            onBackPressed()
        } else {
            handleBackPress(
                webView = webView,
                lastBackPressTime = lastBackPressTime,
                onBackPressed = onBackPressed,
                onTimeUpdate = { lastBackPressTime = it },
                context = context
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showNoInternet) {
            // Show No Internet screen with reload button
            ErrorScreen(
                errorMessage = "No internet connection. Please check your connection and try again.",
                onRetry = {
                    showNoInternet = false
                    isLoading = true
                    // When internet returns, reload the current page
                    // The WebView will maintain its navigation stack
                    webView?.reload()
                }
            )
        } else {
            // Show WebView
            AndroidView(
                factory = { ctx ->
                    createWebView(
                        context = ctx,
                        url = url,
                        onWebViewCreated = { webView = it },
                        onError = {
                            showNoInternet = true
                            onWebViewError()
                        },
                        onLoadStarted = { isLoading = true },
                        onLoadFinished = { isLoading = false },
                        onExternalNavigation = onExternalNavigation,
                        onUrlChanged = { newUrl ->
                            // Track navigation stack
                            if (newUrl != null && newUrl != navigationStack.lastOrNull()) {
                                navigationStack = navigationStack + newUrl
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun createWebView(
    context: Context,
    url: String,
    onWebViewCreated: (WebView) -> Unit,
    onError: () -> Unit,
    onLoadStarted: () -> Unit,
    onLoadFinished: () -> Unit,
    onExternalNavigation: () -> Unit,
    onUrlChanged: (String?) -> Unit
): WebView {
    return WebView(context).apply {
        onWebViewCreated(this)

        configureWebViewSettings()
        configureCookies()
        configureWebViewClient(
            onError = onError,
            onLoadStarted = onLoadStarted,
            onLoadFinished = onLoadFinished,
            onExternalNavigation = onExternalNavigation,
            onUrlChanged = onUrlChanged
        )
        configureWebChromeClient(context)

        loadUrl(url)
        setupFocus()
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureWebViewSettings() {
    settings.apply {
        // JavaScript and DOM
        javaScriptEnabled = true
        domStorageEnabled = true

        // Prevent multiple windows to avoid invisible WebViews
        setSupportMultipleWindows(false)
        javaScriptCanOpenWindowsAutomatically = false

        // Zoom and viewport
        setSupportZoom(true)
        builtInZoomControls = true
        displayZoomControls = false
        loadWithOverviewMode = true
        useWideViewPort = true

        // File access and content
        allowFileAccess = true
        allowContentAccess = true
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        cacheMode = WebSettings.LOAD_DEFAULT

        // Enable file downloads
        setSupportMultipleWindows(true)
        javaScriptCanOpenWindowsAutomatically = true

        // Custom user agent
        userAgentString = "$userAgentString MaatCha/1.0"
    }
}

private fun WebView.configureCookies() {
    CookieManager.getInstance().apply {
        setAcceptCookie(true)
        setAcceptThirdPartyCookies(this@configureCookies, true)
    }
}

private fun WebView.configureWebViewClient(
    onError: () -> Unit,
    onLoadStarted: () -> Unit,
    onLoadFinished: () -> Unit,
    onExternalNavigation: () -> Unit,
    onUrlChanged: (String?) -> Unit
) {
    webViewClient = object : WebViewClient() {
        override fun onPageStarted(
            view: WebView?,
            url: String?,
            favicon: android.graphics.Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)
            onLoadStarted()
            onUrlChanged(url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            onLoadFinished()
            onUrlChanged(url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)

            // Only trigger error for main frame requests (not sub-resources)
            if (request?.isForMainFrame == true) {
                // Check if it's a network-related error
                val errorCode = error?.errorCode ?: 0
                if (isNetworkError(errorCode)) {
                    onError()
                }
            }
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url?.toString() ?: return false
            return handleUrlLoading(url, view, context, onExternalNavigation)
        }

        @Suppress("DEPRECATION")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            val urlString = url ?: return false
            return handleUrlLoading(urlString, view, context, onExternalNavigation)
        }
    }
}

private fun WebView.configureWebChromeClient(context: Context) {
    webChromeClient = object : WebChromeClient() {
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            // Handle file downloads and new windows
            val newWebView = WebView(context)
            newWebView.settings.javaScriptEnabled = true
            newWebView.webChromeClient = this
            
            newWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let { urlString ->
                        // Handle file downloads
                        if (isDownloadableFile(urlString)) {
                            downloadFile(context, urlString)
                            return true
                        }
                        
                        // Load in main WebView
                        this@configureWebChromeClient.loadUrl(urlString)
                    }
                    return true
                }
            }
            
            val transport = resultMsg?.obj as? WebView.WebViewTransport
            transport?.webView = newWebView
            resultMsg?.sendToTarget()
            return true
        }
        
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: android.webkit.ValueCallback<Array<android.net.Uri>>?,
            fileChooserParams: android.webkit.WebChromeClient.FileChooserParams?
        ): Boolean {
            // Handle file uploads if needed
            return false
        }
    }
}

private fun WebView.setupFocus() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
}

/**
 * Checks if the error code represents a network-related error
 */
private fun isNetworkError(errorCode: Int): Boolean {
    return when (errorCode) {
        WebViewClient.ERROR_HOST_LOOKUP,
        WebViewClient.ERROR_CONNECT,
        WebViewClient.ERROR_TIMEOUT,
        WebViewClient.ERROR_REDIRECT_LOOP,
        WebViewClient.ERROR_UNSUPPORTED_SCHEME,
        WebViewClient.ERROR_FAILED_SSL_HANDSHAKE,
        WebViewClient.ERROR_BAD_URL,
        WebViewClient.ERROR_FILE_NOT_FOUND,
        WebViewClient.ERROR_FILE,
        WebViewClient.ERROR_PROXY_AUTHENTICATION,
        WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME -> true

        else -> false
    }
}

/**
 * Checks if a URL points to a downloadable file
 */
private fun isDownloadableFile(url: String): Boolean {
    val downloadableExtensions = listOf(
        ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
        ".zip", ".rar", ".7z", ".tar", ".gz",
        ".mp3", ".mp4", ".avi", ".mov", ".wmv",
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff",
        ".txt", ".rtf", ".csv"
    )
    
    return downloadableExtensions.any { url.lowercase().endsWith(it) }
}

/**
 * Initiates file download
 */
private fun downloadFile(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to download file", Toast.LENGTH_SHORT).show()
    }
}

private fun handleUrlLoading(
    url: String,
    webView: WebView?,
    context: Context,
    onExternalNavigation: () -> Unit
): Boolean {
    return when {
        url.startsWith("tel:") -> {
            handlePhoneCall(url, context)
            true
        }

        url.startsWith("mailto:") -> {
            handleEmail(url, context)
            true
        }

        url.startsWith("http://") || url.startsWith("https://") -> {
            // Check if it's a downloadable file
            if (isDownloadableFile(url)) {
                downloadFile(context, url)
                return true
            }
            
            // Load all HTTP/HTTPS URLs in the main WebView
            webView?.loadUrl(url)
            true
        }

        else -> {
            // Handle other schemes as external navigation
            onExternalNavigation()
            false
        }
    }
}

private fun handlePhoneCall(phoneUrl: String, context: Context) {
    val intent = Intent(Intent.ACTION_DIAL, phoneUrl.toUri())
    context.startActivity(intent)
}

private fun handleEmail(emailUrl: String, context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO, emailUrl.toUri())
    context.startActivity(intent)
}

private fun handleBackPress(
    webView: WebView?,
    lastBackPressTime: Long,
    onBackPressed: () -> Unit,
    onTimeUpdate: (Long) -> Unit,
    context: Context
) {
    webView?.let { wv ->
        if (wv.canGoBack()) {
            // Navigate back in WebView history
            wv.goBack()
        } else {
            // Cannot go back further in WebView - exit app with double-tap
            val now = System.currentTimeMillis()
            if (now - lastBackPressTime < SplashConstants.DOUBLE_TAP_TIMEOUT_MS) {
                onBackPressed()
            } else {
                onTimeUpdate(now)
                Toast.makeText(context, SplashConstants.TOAST_PRESS_BACK_AGAIN, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    } ?: run {
        // No WebView available - exit app
        onBackPressed()
    }
}
