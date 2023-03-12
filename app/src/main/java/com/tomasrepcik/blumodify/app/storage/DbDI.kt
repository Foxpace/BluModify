package com.tomasrepcik.blumodify.app.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.tomasrepcik.blumodify.app.storage.cache.*
import com.tomasrepcik.blumodify.app.storage.room.AppDatabase
import com.tomasrepcik.blumodify.app.storage.room.BtDeviceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbDI {

    @Provides
    @Singleton
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): DataStore<AppSettings> =
        context.settingsDataStore

    @Provides
    @Singleton
    fun provideAppCache(dataStore: DataStore<AppSettings>): AppCacheTemplate<AppCacheState> =
        AppCache(dataStore)

    @Provides
    @Singleton
    fun provideBtDatabase(@ApplicationContext context: Context): BtDeviceDao {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "BluModifyDb"
        ).build()
        return db.btDao()
    }
}