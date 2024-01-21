package com.vzkz.profinder.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.R
import com.vzkz.profinder.core.boilerplate.SERVICELISTFORTEST
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Constants
import com.vzkz.profinder.domain.model.Constants.CONNECTION_ERROR
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.ServiceModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val context: Context
) : Repository {

    //Firestore
    override suspend fun login(email: String, password: String): ActorModel? {
        val user: FirebaseUser?
        try{
//            for(service in SERVICELISTFORTEST){ todo delete
//                firestoreService.insertService(service)
//            }
            user = authService.login(email, password)
        } catch (e: Exception){
            throw Exception(context.getString(R.string.wrong_email_or_password))
        }
        return if(user != null) getUserFromFirestore(user.uid)
        else null
    }

    override suspend fun getUserFromFirestore(uid: String): ActorModel {
        try {
            return firestoreService.getUserData(uid)
        } catch (e: Exception) {
            when (e.message) {
                CONNECTION_ERROR -> throw Exception(context.getString(R.string.network_failure_while_checking_user_existence))
                else -> throw Exception(context.getString(R.string.unknown_exception_occurred_during_login))
            }
        }
    }

    override suspend fun getServiceListFromFirestore(uid: String): List<ServiceModel>{
        return firestoreService.getServiceList(uid)
    }

    override fun insertServiceInFirestore(service: ServiceModel){
        firestoreService.insertService(service)
    }

    override fun deleteService(sid: String){
        firestoreService.deleteService(sid)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): ActorModel {
        if (firestoreService.nicknameExists(nickname)) {
            throw Exception(context.getString(R.string.username_already_in_use))
        } else {
            val user: ActorModel
            try {
                val firestoreUser = authService.signUp(email, password)
                if (firestoreUser != null) {
                    user = ActorModel(
                        nickname = nickname,
                        uid = firestoreUser.uid,
                        firstname = firstname,
                        lastname = lastname,
                        actor = actor,
                        profession = profession
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
    override suspend fun modifyUserData(oldUser: ActorModel, newUser: ActorModel) {
        try{
            firestoreService.modifyUserData(oldUser = oldUser, newUser = newUser)
        } catch (e: Exception){
            if(e.message == Constants.NICKNAME_IN_USE) throw Exception(context.getString(R.string.nickname_already_in_use_couldn_t_modify_user))
            else throw Exception(context.getString(R.string.error_modifying_user_data_the_user_wasn_t_modified))
        }
    }
    override fun changeProfState(uid: String, state: ProfState){
        firestoreService.changeProfState(uid, state)
    }

    override fun modifyServiceActivity(sid: String, newValue: Boolean){
        firestoreService.modifyServiceActivity(sid, newValue)
    }

}