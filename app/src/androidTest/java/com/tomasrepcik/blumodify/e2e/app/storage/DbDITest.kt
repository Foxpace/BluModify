package com.tomasrepcik.blumodify.e2e.app.storage

import android.content.Context
import androidx.room.Room
import com.tomasrepcik.blumodify.app.storage.AppCacheDi
import com.tomasrepcik.blumodify.app.storage.DbDI
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.room.AppDatabase
import com.tomasrepcik.blumodify.e2e.app.storage.cache.AppCacheInMemory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppCacheDi::class]
)
abstract class AppCacheDiTest {
    @Binds
    @Singleton
    abstract fun provideAppCache(appCache: AppCacheInMemory): AppCacheTemplate<AppCacheState>

}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DbDI::class]
)
object DbDITest {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
}