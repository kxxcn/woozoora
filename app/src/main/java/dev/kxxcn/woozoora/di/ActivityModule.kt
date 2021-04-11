package dev.kxxcn.woozoora.di

import android.app.Activity
import dagger.Binds
import dagger.Module
import dev.kxxcn.woozoora.WoozooraActivity

@Module(includes = [IntroModule::class])
abstract class ActivityModule {

    @Binds
    abstract fun bindActivity(activity: WoozooraActivity): Activity
}
