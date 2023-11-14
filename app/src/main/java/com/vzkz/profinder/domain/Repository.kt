package com.vzkz.profinder.domain

import com.vzkz.profinder.domain.model.UserModel

interface Repository {

    suspend fun login(email: String, password: String): UserModel?

    suspend fun signUp(email: String, password: String, nickname: String): UserModel?

    suspend fun logout()

    fun isUserLogged(): Boolean

}