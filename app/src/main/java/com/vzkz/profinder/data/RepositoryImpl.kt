package com.vzkz.profinder.data

import android.content.res.Resources
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.R
import com.vzkz.profinder.data.database.dao.UserDao
import com.vzkz.profinder.data.database.entities.UserEntity
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import com.vzkz.profinder.domain.model.toDomain
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val userDao: UserDao
) : Repository {

    override suspend fun login(email: String, password: String): Pair<UserModel?, Boolean>? { //POSSIBLE ERROR OF DATA CONSISTENCY BECAUSE OF ROOM
        val user = authService.login(email, password)
        return if(user != null){ //At this point, user should never be null
            val tryLocalDB = userDao.getUserByUid(user.uid)
            if(tryLocalDB != null){ //We try to get the user from local DB
                return Pair(UserModel(nickname = tryLocalDB.nickname, uid = tryLocalDB.uid), true)
            } else { //We don't have the user in our local DB so we get it from Firestore
                val nickname =
                    firestoreService.getUserData(user.uid) //At this point, user should never be null
                Pair(user.toDomain(nickname), false)
            }
        } else null
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
    override suspend fun getUserFromDB(nickname: String): UserModel? {
        val response: UserEntity?
        return try {
            response = userDao.getUser(nickname)
            response?.toDomain()
        } catch(e: Exception) {
            Log.e("Jaime", "Error accesing a user in room (Exception controlled: ${e.message}")
            null
        }
    }

    override suspend fun insertUserToDB(user: UserModel) {
        userDao.insertUser(UserEntity(user.nickname, user.uid))
    }

    override suspend fun clearUsers() {
        userDao.clearUsers()
    }

    private fun FirebaseUser.toDomain(nickname: String): UserModel {
        return UserModel(uid = this.uid, nickname = nickname)
    }

}