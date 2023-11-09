package com.vzkz.profinder.data

import com.google.firebase.auth.FirebaseAuth
import com.vzkz.profinder.data.auth.AuthService
import com.vzkz.profinder.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    fun provideRepository(authService: AuthService): Repository {
        return RepositoryImpl(authService)
    }
}