package com.maat.cha.feature.splash.state

import com.maat.cha.core.data.model.MatchViewResponse

data class SplashState(
    val isLoading: Boolean = true,
    val isApiLoading: Boolean = false,
    val apiResponse: MatchViewResponse? = null,
    val error: String? = null,
    val showWebView: Boolean = false,
    val showBanner: Boolean = false,
    val webViewUrl: String? = null,
    val bannerUrl: String? = null,
    val hasNavigatedExternally: Boolean = false,
    val originalApiResponse: MatchViewResponse? = null
) 