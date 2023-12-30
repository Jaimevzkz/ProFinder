package com.vzkz.profinder.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.R
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.Constants
import com.vzkz.profinder.domain.model.Constants.CONNECTION_ERROR
import com.vzkz.profinder.domain.model.Constants.MODIFICATION_ERROR
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val context: Context
) : Repository {

    //Firestore
    override suspend fun login(email: String, password: String): UserModel? {
        val user: FirebaseUser?
        try{
            user = authService.login(email, password)
        } catch (e: Exception){
            throw Exception(context.getString(R.string.wrong_email_or_password))
        }
        return if(user != null) getUserFromFirestore(user.uid)
        else null
    }

    override suspend fun getUserFromFirestore(uid: String): UserModel {
        try {
            return firestoreService.getUserData(uid)
        } catch (e: Exception) {
            when (e.message) {
                CONNECTION_ERROR -> throw Exception(context.getString(R.string.network_failure_while_checking_user_existence))
//                else -> throw Exception(context.getString(R.string.couldn_t_find_the_user))
                else -> throw Exception("Unknown exception ocurr during login")
            }
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String
    ): UserModel {
        if (firestoreService.nicknameExists(nickname)) {
            throw Exception(context.getString(R.string.username_already_in_use))
        } else {
            val user: UserModel
            try {
                val firestoreUser = authService.signUp(email, password)
                if (firestoreUser != null) {
                    user = UserModel(
                        nickname = nickname,
                        uid = firestoreUser.uid
                    )
                } else {
                    throw Exception()
                }
            } catch (e: Exception){
                throw Exception(context.getString(R.string.account_already_exists))
            }
            try{
                firestoreService.insertUser(user)
            } catch (e: Exception){
                throw Exception(context.getString(R.string.couldn_t_insert_user_in_database))
            }
            return user
        }
    }

    override suspend fun logout() = authService.logout()

    override fun isUserLogged() = authService.isUserLogged()
    override suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel) {
        try{
            firestoreService.modifyUserData(oldUser = oldUser, newUser = newUser)
        } catch (e: Exception){
            if(e.message == Constants.NICKNAME_IN_USE) throw Exception(context.getString(R.string.nickname_already_in_use_couldn_t_modify_user))
            else throw Exception(context.getString(R.string.error_modifying_user_data_the_user_wasn_t_modified))
        }
    }

}