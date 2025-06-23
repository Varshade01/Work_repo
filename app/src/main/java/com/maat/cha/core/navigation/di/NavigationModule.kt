package com.maat.cha.core.navigation.di

import com.maat.cha.core.navigation.actions.GameNavActionsImpl
import com.maat.cha.core.navigation.actions.InfoNavActionsImpl
import com.maat.cha.core.navigation.actions.MainNavActionsImpl
import com.maat.cha.core.navigation.actions.ReferenceInfoNavActionsImpl
import com.maat.cha.core.navigation.actions.SettingsNavActionsImpl
import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.core.navigation.navigator.AppNavigatorImpl
import com.maat.cha.feature.appinfo.navigation.InfoNavigationActions
import com.maat.cha.feature.appinfo.navigation.ReferenceInfoNavigationActions
import com.maat.cha.feature.game.navigation.GameNavigationActions
import com.maat.cha.feature.main.navigation.MainNavigationActions
import com.maat.cha.feature.settings.navigation.SettingsNavigationActions
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindAppNavigator(
        appNavigatorImpl: AppNavigatorImpl
    ): AppNavigator

    @Binds
    @Singleton
    abstract fun bindMainNavActions(
        mainNavActionsImpl: MainNavActionsImpl
    ): MainNavigationActions

    @Binds
    @Singleton
    abstract fun bindInfoNavigationActions(
        infoNavActionsImpl: InfoNavActionsImpl
    ): InfoNavigationActions

    @Binds
    @Singleton
    abstract fun bindGameNavigationActions(
        gameNavActionsImpl: GameNavActionsImpl
    ): GameNavigationActions

    @Binds
    @Singleton
    abstract fun bindSettingsNavigationActions(
        settingsNavActionsImpl: SettingsNavActionsImpl
    ): SettingsNavigationActions

    @Binds
    @Singleton
    abstract fun bindReferenceInfoNavigationActions(
        referenceInfoNavActionsImpl: ReferenceInfoNavActionsImpl
    ): ReferenceInfoNavigationActions
}