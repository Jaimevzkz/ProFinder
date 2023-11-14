package com.vzkz.profinder.domain

import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveUser(user: UserModel)

    suspend fun getUser(): UserModel

    suspend fun setAppTheme(): Boolean

    suspend fun getAppTheme(): Flow<Boolean>
}