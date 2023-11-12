package com.vzkz.profinder.data.di

import android.content.Context
import androidx.room.Room
import com.vzkz.profinder.data.database.UserDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val QUOTE_DB_NAME = "user_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, UserDB::class.java, QUOTE_DB_NAME).build()

    @Singleton
    @Provides
    fun provideQuoteDao(db: UserDB) = db.getUserDao()
}