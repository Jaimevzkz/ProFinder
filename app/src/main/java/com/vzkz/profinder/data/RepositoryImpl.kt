package com.vzkz.profinder.data

import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService
) : Repository {

    override suspend fun login(email: String, password: String): UserModel? {
        val user = authService.login(email, password)
        if(user != null){
            val nickname: String = firestoreService.getUserData(user.uid)
            return user.toDomain(nickname)
        }
        return null
    }

    override suspend fun signUp(email: String, password: String, nickname: String): UserModel? {
        if (firestoreService.userExists(nickname)) {
            throw Exception("Username already in use")
        } else {
            val user = authService.signUp(email, password)?.toDomain(nickname)
            firestoreService.insertUser(user) //At this point, user should never be null
            return user
        }
    }

    override suspend fun logout() = authService.logout()

    override fun isUserLogged() = authService.isUserLogged()

    private fun FirebaseUser.toDomain(nickname: String): UserModel {
        return UserModel(uid = this.uid, nickname = nickname)
    }

}