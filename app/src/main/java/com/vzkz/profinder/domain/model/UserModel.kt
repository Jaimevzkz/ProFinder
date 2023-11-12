package com.vzkz.profinder.domain.model

import com.vzkz.profinder.data.database.entities.UserEntity

data class UserModel(val nickname: String, val uid: String)

fun UserEntity.toDomain() = UserModel(nickname = nickname, uid = uid)