package com.vzkz.profinder.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vzkz.profinder.data.UserFields.NICKNAME
import com.vzkz.profinder.data.UserFields.UID
import com.vzkz.profinder.domain.DataStoreRepository
import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val PREFERENCES_NAME = "my_preferences"

private object UserFields {
    const val UID = "uid"
    const val NICKNAME = "nickname"
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepositoryImpl @Inject constructor(private val context: Context) :
    DataStoreRepository {
    override suspend fun saveUser(user: UserModel) {
        saveUserField(UID, user.uid)
        saveUserField(NICKNAME, user.nickname)
    }

    private suspend fun saveUserField(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getUser(): UserModel {
        val preferences = context.dataStore.data.first()
        val uid = preferences[stringPreferencesKey(UID)]?: "no"
        return  UserModel(uid = preferences[stringPreferencesKey(UID)]?: "", nickname = preferences[stringPreferencesKey(NICKNAME)]?: "")


//        return context.dataStore.data.map { preferences ->
//            if(preferences[stringPreferencesKey(NICKNAME)] != null){
//                UserModel(
//                    uid = preferences[stringPreferencesKey(UID)] ?: "",
//                    nickname = preferences[stringPreferencesKey(NICKNAME)] ?: ""
//                )
//            }
//            else null
//
//        }

    }
}