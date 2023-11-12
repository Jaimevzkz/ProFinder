package com.vzkz.profinder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vzkz.profinder.data.database.dao.UserDao
import com.vzkz.profinder.data.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDB: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}