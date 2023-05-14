package com.tomasrepcik.blumodify.app.notifications

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationDi {
    @Binds
    @Singleton
    abstract fun provideNotificationRepo(notificationRepo: NotificationRepo): NotificationRepoTemplate
}