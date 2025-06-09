package com.maat.cha.core.navigation.di

import com.maat.cha.core.navigation.navigator.AppNavigator
import com.maat.cha.core.navigation.navigator.AppNavigatorImpl
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

    //  @Binds
    //  @Singleton
    //  abstract fun bindMainNavActions(
    //      mainNavActionsImpl: MainNavActionsImpl
    //  ): MainNavActions

    // @Singleton
    // @Binds
    // abstract fun bindSplashNavigationActions(
    //     splashNavigationActions: SplashNavigationActionsImpl
    // ): SplashNavigationActions


}