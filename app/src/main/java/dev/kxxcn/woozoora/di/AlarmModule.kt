package dev.kxxcn.woozoora.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.woozoora.receiver.AlarmReceiver

@Module
abstract class AlarmModule {

    @ContributesAndroidInjector
    internal abstract fun receiver(): AlarmReceiver
}
