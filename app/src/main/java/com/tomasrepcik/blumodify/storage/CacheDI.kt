package com.tomasrepcik.blumodify.storage

import android.content.Context
import androidx.datastore.core.DataStore
import com.tomasrepcik.blumodify.storage.datastore.AppSettings
import com.tomasrepcik.blumodify.storage.datastore.settingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object CacheDI {

    @Provides
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): DataStore<AppSettings> =
        context.settingsDataStore

    @Provides
    fun provideAppCache(dataStore: DataStore<AppSettings>): AppCache<AppCacheState> =
        AppCacheImp(dataStore)


}