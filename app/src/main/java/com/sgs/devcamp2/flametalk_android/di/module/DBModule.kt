package com.sgs.devcamp2.flametalk_android.di.module

import android.content.Context
import androidx.room.Room
import com.sgs.devcamp2.flametalk_android.data.source.local.dao.ChatRoomListDao
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author boris
 * @created 2022/01/27
 */
@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideChatRoomListDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "ChatRoomListDatabase.db")
        .build()

    @Singleton
    @Provides
    fun provideChatRoomListDao(appDatabase: AppDatabase): ChatRoomListDao = appDatabase.chatRoomListDao()
}
