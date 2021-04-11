package dev.kxxcn.woozoora.di

import dagger.Binds
import dagger.Module
import dev.kxxcn.woozoora.common.base.NotificationProvider
import dev.kxxcn.woozoora.util.NotificationFactory

@Module
internal abstract class NotificationBuilder {

    @Binds
    internal abstract fun bindNotificationFactory(
        factory: NotificationFactory,
    ): NotificationProvider.Factory
}
