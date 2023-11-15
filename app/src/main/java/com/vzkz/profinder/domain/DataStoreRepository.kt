package com.vzkz.profinder.domain

import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveUser(user: UserModel)

    suspend fun getUser(): Flow<UserModel>

    suspend fun switchAppTheme()

    suspend fun getAppTheme(): Flow<Boolean>
}