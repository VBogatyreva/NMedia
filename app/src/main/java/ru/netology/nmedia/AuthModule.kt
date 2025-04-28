package ru.netology.nmedia

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {
    @Provides
    @Singleton
    fun provideAppAuth(
        authHolder: AuthHolder,
        repository: PostRepository,
        apiService: PostApiService
    ): AppAuth = AppAuth(authHolder, repository, apiService)
}