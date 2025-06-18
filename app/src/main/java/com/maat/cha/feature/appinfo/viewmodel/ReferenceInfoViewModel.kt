package com.maat.cha.feature.appinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.feature.appinfo.navigation.ReferenceInfoNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReferenceInfoViewModel @Inject constructor(
    private val navigationActions: ReferenceInfoNavigationActions
) : ViewModel() {

    fun onGotItClick() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }
} 