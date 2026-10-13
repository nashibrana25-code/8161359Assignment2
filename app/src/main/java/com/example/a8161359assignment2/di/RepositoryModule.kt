package com.example.a8161359assignment2.di

import com.example.a8161359assignment2.data.repository.AppRepository
import com.example.a8161359assignment2.data.repository.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        implementation: AppRepositoryImpl
    ): AppRepository
}