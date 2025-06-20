package com.maat.cha.feature.splash.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import androidx.core.net.toUri

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComposable(
    url: String,
    onBackPressed: () -> Unit,
    onExternalNavigation: () -> Unit = {},
    onWebViewError: () -> Unit = {}
) {
    val context = LocalContext.current
    // Дозвіл дебагу WebView (можна вимкнути в релізі)
    LaunchedEffect(Unit) {
        WebView.setWebContentsDebuggingEnabled(true)
    }

    var webView by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableIntStateOf(0) }
    var hasLoadedSuccessfully by remember { mutableStateOf(false) }
    var lastBackPressTime by remember { mutableStateOf(0L) }

    // Обробка кнопки «Назад»
    BackHandler {
        webView?.let { wv ->
            if (wv.canGoBack()) {
                wv.goBack()
            } else {
                val now = System.currentTimeMillis()
                if (now - lastBackPressTime < 2000) {
                    onBackPressed()
                } else {
                    lastBackPressTime = now
                    Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    webView = this
                    // Налаштування WebView
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true

                        // Вимкнути multiple windows, щоб всі посилання відкривались в цьому WebView
                        setSupportMultipleWindows(false)
                        javaScriptCanOpenWindowsAutomatically = false

                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                        loadWithOverviewMode = true
                        useWideViewPort = true

                        allowFileAccess = true
                        allowContentAccess = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cacheMode = WebSettings.LOAD_DEFAULT

                        // Інше за потреби...
                    }
                    CookieManager.getInstance().setAcceptCookie(true)
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                    settings.userAgentString = settings.userAgentString + " MaatCha/1.0"

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            progress = 0
                        }
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            progress = 100
                            hasLoadedSuccessfully = true
                        }
                        override fun onReceivedError(
                            view: WebView?, request: WebResourceRequest?, error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            if (!hasLoadedSuccessfully) {
                                onWebViewError()
                            }
                        }
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            val u = request?.url?.toString() ?: return false
                            return handleSpecialUrl(u, view, context, onExternalNavigation)
                        }
                        @Suppress("DEPRECATION")
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            val u = url ?: return false
                            return handleSpecialUrl(u, view, context, onExternalNavigation)
                        }
                        private fun handleSpecialUrl(
                            urlString: String,
                            view: WebView?,
                            ctx: Context,
                            onExternalNavigation: () -> Unit
                        ): Boolean {
                            return when {
                                urlString.startsWith("tel:") -> {
                                    ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(urlString)))
                                    true
                                }
                                urlString.startsWith("mailto:") -> {
                                    ctx.startActivity(Intent(Intent.ACTION_SENDTO, urlString.toUri()))
                                    true
                                }
                                urlString.startsWith("http://") || urlString.startsWith("https://") -> {
                                    // Завантажити всередині WebView
                                    view?.loadUrl(urlString)
                                    true
                                }
                                else -> {
                                    // Інші схеми можна обробити тут; якщо зовнішня навігація:
                                    onExternalNavigation()
                                    false
                                }
                            }
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onCreateWindow(
                            view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: android.os.Message?
                        ): Boolean {
                            // Заборонити створення нового вікна
                            return false
                        }
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            progress = newProgress
                        }
                    }

                    // Завантажити URL
                    loadUrl(url)

                    // Надаємо фокус для кліків
                    isFocusable = true
                    isFocusableInTouchMode = true
                    requestFocus()
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading && progress < 100) {
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                color = Color(0xFFFDB001),
                trackColor = Color(0xFFFDB001).copy(alpha = 0.3f)
            )
        }
    }
}
