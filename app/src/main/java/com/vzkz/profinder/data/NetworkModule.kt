package com.vzkz.profinder.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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


    @Singleton
    @Provides
    fun provideRepository(
        authService: AuthService,
        firestoreService: FirestoreService
    ): Repository {
        return RepositoryImpl(authService, firestoreService)
    }

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore
}