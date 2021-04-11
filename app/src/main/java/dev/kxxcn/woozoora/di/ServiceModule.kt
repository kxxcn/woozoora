package dev.kxxcn.woozoora.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.woozoora.service.FirebaseMessagingService

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    internal abstract fun service(): FirebaseMessagingService
}
