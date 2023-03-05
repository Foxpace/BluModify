package com.tomasrepcik.blumodify.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.tomasrepcik.blumodify.storage.cache.*
import com.tomasrepcik.blumodify.storage.room.AppDatabase
import com.tomasrepcik.blumodify.storage.room.BtDeviceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbDI {

    @Provides
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): DataStore<AppSettings> =
        context.settingsDataStore

    @Provides
    fun provideAppCache(dataStore: DataStore<AppSettings>): AppCacheTemplate<AppCacheState> =
        AppCache(dataStore)

    @Provides
    fun provideBtDatabase(@ApplicationContext context: Context): BtDeviceDao {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "BluModifyDb"
        ).build()
        return db.btDao()
    }
}