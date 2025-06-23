package com.maat.cha.feature.di

import com.maat.cha.feature.splash.navigations.SplashNavigationActions
import com.maat.cha.feature.splash.navigations.SplashNavigationActionsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FeatureModule {
    
    @Binds
    abstract fun bindSplashNavigationActions(
        splashNavigationActionsImpl: SplashNavigationActionsImpl
    ): SplashNavigationActions
} 