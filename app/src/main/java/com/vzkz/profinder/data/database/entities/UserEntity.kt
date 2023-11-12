package com.vzkz.profinder.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo("nickname") val nickname: String,
    @ColumnInfo("uid") val uid: String
)