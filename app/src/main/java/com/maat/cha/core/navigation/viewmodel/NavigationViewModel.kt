package com.maat.cha.core.navigation.viewmodel

import com.maat.cha.core.navigation.navigator.AppNavigator
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    appNavigator: AppNavigator
) : ViewModel() {
    val navigationFlow = appNavigator.navigationFlow
}