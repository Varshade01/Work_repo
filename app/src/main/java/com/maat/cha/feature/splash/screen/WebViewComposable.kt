package com.maat.cha.feature.splash.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
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
    
    // Enable WebView debugging for development
    LaunchedEffect(Unit) {
        WebView.setWebContentsDebuggingEnabled(true)
    }

    var webView by remember { mutableStateOf<WebView?>(null) }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }

    // Handle back button with double-tap exit
    BackHandler {
        handleBackPress(
            webView = webView,
            lastBackPressTime = lastBackPressTime,
            onBackPressed = onBackPressed,
            onTimeUpdate = { lastBackPressTime = it },
            context = context
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                createWebView(
                    context = ctx,
                    url = url,
                    onWebViewCreated = { webView = it },
                    onError = onWebViewError,
                    onExternalNavigation = onExternalNavigation
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun createWebView(
    context: Context,
    url: String,
    onWebViewCreated: (WebView) -> Unit,
    onError: () -> Unit,
    onExternalNavigation: () -> Unit
): WebView {
    return WebView(context).apply {
        onWebViewCreated(this)
        
        configureWebViewSettings()
        configureCookies()
        configureWebViewClient(
            onError = onError,
            onExternalNavigation = onExternalNavigation
        )
        configureWebChromeClient()
        
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
    onExternalNavigation: () -> Unit
) {
    webViewClient = object : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            onError()
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

private fun WebView.configureWebChromeClient() {
    webChromeClient = object : WebChromeClient() {
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            // Prevent creation of invisible WebViews
            return false
        }
    }
}

private fun WebView.setupFocus() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
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
            wv.goBack()
        } else {
            val now = System.currentTimeMillis()
            if (now - lastBackPressTime < SplashConstants.DOUBLE_TAP_TIMEOUT_MS) {
                onBackPressed()
            } else {
                onTimeUpdate(now)
                Toast.makeText(context, SplashConstants.TOAST_PRESS_BACK_AGAIN, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
