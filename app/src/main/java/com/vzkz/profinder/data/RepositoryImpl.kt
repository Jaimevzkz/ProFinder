package com.vzkz.profinder.data

import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.data.auth.AuthService
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val authService: AuthService) : Repository {

    override suspend fun login(email: String, password: String): UserModel? {
        return authService.login(email, password)?.toDomain()
    }

    override suspend fun signUp(email: String, password: String): UserModel? {
        return authService.signUp(email, password)?.toDomain()
    }

    private fun FirebaseUser.toDomain(): UserModel{
        return UserModel(this.uid)
    }

}