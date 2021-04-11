package dev.kxxcn.woozoora.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.woozoora.WoozooraActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(
        modules = [
            ActivityModule::class,
            SignInModule::class
        ]
    )
    internal abstract fun activity(): WoozooraActivity
}
