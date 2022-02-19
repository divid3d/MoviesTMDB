package com.example.moviesapp.di

import com.example.moviesapp.initializer.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Binds
    @IntoSet
    abstract fun provideFirebaseInitializer(
        initializer: FirebaseInitializer
    ): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideConfigRepositoryInitializer(
        initializer: ConfigDataSourceInitializer
    ): AppInitializer

}