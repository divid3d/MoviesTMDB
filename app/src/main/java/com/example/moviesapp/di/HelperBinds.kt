package com.example.moviesapp.di

import com.example.moviesapp.other.TextRecognitionHelper
import com.example.moviesapp.other.TextRecognitionHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HelperBinds {
    @Binds
    fun provideTextRecognitionHelper(impl: TextRecognitionHelperImpl): TextRecognitionHelper
}