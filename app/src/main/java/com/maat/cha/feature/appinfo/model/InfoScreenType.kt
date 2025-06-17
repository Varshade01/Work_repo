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
        contentRes = R.string.how_to_play,
        bottomBtnTextRes = R.string.text_reject,
        mainButtonTextRes = R.string.agree
    )

    object Privacy : InfoScreenType(
        titleRes = R.string.title_privacy,
        contentRes = R.string.text_privacy,
        bottomBtnTextRes = R.string.text_reject,
        mainButtonTextRes = R.string.accept
    )
}

