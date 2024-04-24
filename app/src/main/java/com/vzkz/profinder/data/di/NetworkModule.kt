package com.vzkz.profinder.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.vzkz.profinder.core.UidCombiner
import com.vzkz.profinder.data.DataStoreRepositoryImpl
import com.vzkz.profinder.data.ILocationService
import com.vzkz.profinder.data.RepositoryImpl
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.data.firebase.RealtimeService
import com.vzkz.profinder.data.firebase.StorageService
import com.vzkz.profinder.domain.DataStoreRepository
import com.vzkz.profinder.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRepository(
        authService: AuthService,
        firestoreService: FirestoreService,
        storageService: StorageService,
        realtimeService: RealtimeService,
        uidCombiner: UidCombiner,
        locationService: ILocationService
    ): Repository {
        return RepositoryImpl(authService, firestoreService, storageService, realtimeService, uidCombiner, locationService)
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository = DataStoreRepositoryImpl(app)

    //Firebase
    @Singleton
    @Provides
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage

    @Singleton
    @Provides
    fun provideRealtimeDB(): DatabaseReference = Firebase.database.reference

    @Singleton
    @Provides
    fun provideRealtimeService(reference: DatabaseReference) = RealtimeService(reference)

    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO
}