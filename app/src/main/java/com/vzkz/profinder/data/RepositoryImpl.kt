package com.vzkz.profinder.data

import android.content.res.Resources
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.R
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService
) : Repository {

    override suspend fun login(email: String, password: String): UserModel? {
        val user = authService.login(email, password)
        val nickname =
            firestoreService.getUserData(user?.uid) //At this point, user should never be null
        return user?.toDomain(nickname)
    }

    override suspend fun signUp(email: String, password: String, nickname: String): UserModel? {
        if (firestoreService.userExists(nickname)) {
            throw Exception(Resources.getSystem().getString(R.string.username_already_in_use))
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