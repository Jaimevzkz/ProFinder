package com.vzkz.profinder.domain

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveUid(uid: String)

    suspend fun getUid(): String

    suspend fun switchAppTheme()

    suspend fun getAppTheme(): Flow<Boolean>
}