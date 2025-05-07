package ru.netology.nmedia.auth

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.repository.PostRepository
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