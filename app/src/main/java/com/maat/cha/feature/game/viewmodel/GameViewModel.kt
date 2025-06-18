package com.maat.cha.feature.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.feature.game.navigation.GameNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val navigationActions: GameNavigationActions
) : ViewModel() {

    fun onBackClick() {
        viewModelScope.launch {
            navigationActions.navigateToMain()
        }
    }

    fun onInfoClick() {
        viewModelScope.launch {
            navigationActions.navigateToReferenceInfo(InfoType.HOW_TO_PLAY)
        }
    }
} 