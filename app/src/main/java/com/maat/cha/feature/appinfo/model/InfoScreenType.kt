package com.maat.cha.feature.appinfo.model

import androidx.annotation.StringRes
import com.maat.cha.R

sealed class InfoScreenType(
    @StringRes val titleRes: Int,
    @StringRes val contentRes: Int,
    @StringRes val bottomBtnTextRes: Int? = null,
    @StringRes val mainButtonTextRes: Int
) {
    object HowToPlay : InfoScreenType(
        titleRes = R.string.title_how_to_play,
        contentRes = R.string.how_to_play,
        mainButtonTextRes = R.string.got_it
    )

    object TermsOfUse : InfoScreenType(
        titleRes = R.string.title_terms_of_use,
        contentRes = R.string.terms_of_use_content,
        mainButtonTextRes = R.string.got_it
    )

    object Privacy : InfoScreenType(
        titleRes = R.string.title_privacy,
        contentRes = R.string.text_privacy,
        bottomBtnTextRes = R.string.text_reject,
        mainButtonTextRes = R.string.accept
    )

    object PrivacyPolicy : InfoScreenType(
        titleRes = R.string.title_privacy,
        contentRes = R.string.privacy_policy_content,
        mainButtonTextRes = R.string.got_it
    )

    object PrivacyPolicyWebView : InfoScreenType(
        titleRes = R.string.title_privacy,
        contentRes = R.string.text_privacy,
        mainButtonTextRes = R.string.accept
    ) {
        const val PRIVACY_POLICY_URL = "https://commaatcha.com/policy"
    }

    object TermsOfUsePolicy : InfoScreenType(
        titleRes = R.string.title_terms_of_use,
        contentRes = R.string.terms_of_use_content,
        mainButtonTextRes = R.string.got_it
    )
}

