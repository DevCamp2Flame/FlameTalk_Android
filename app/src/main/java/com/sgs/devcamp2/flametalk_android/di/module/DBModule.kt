package com.sgs.devcamp2.flametalk_android.di.module

import android.content.Context
import androidx.room.Room
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author 김현국
 * @created 2022/01/27
 * AppDatabaseImpl에 AppDatabase 의존성 주입
 */
@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideChatRoomListDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room
        .databaseBuilder(context, AppDatabaseImpl::class.java, AppDatabaseImpl.DB_NAME)
        .allowMainThreadQueries()
        .build()
}
