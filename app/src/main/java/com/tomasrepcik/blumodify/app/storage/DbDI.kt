package com.tomasrepcik.blumodify.app.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.tomasrepcik.blumodify.app.storage.cache.*
import com.tomasrepcik.blumodify.app.storage.room.AppDatabase
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppCacheDi {
    @Binds
    @Singleton
    abstract fun provideAppCache(appCache: AppCache): AppCacheTemplate<AppCacheState>

}
@Module
@InstallIn(SingletonComponent::class)
object DbDI {

    @Provides
    @Singleton
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): DataStore<AppSettings> =
        context.settingsDataStore

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "BluModifyDb"
    ).build()

    @Provides
    @Singleton
    fun provideBtDatabase(db: AppDatabase): BtDeviceDao = db.btDao()

    @Provides
    @Singleton
    fun provideRunReportDatabase(db: AppDatabase): LogsDao = db.runReportDao()
}