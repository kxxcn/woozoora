package dev.kxxcn.woozoora.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.kxxcn.woozoora.WoozooraApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AssistedInjectModule::class,
        ActivityBuilder::class,
        ApplicationModule::class,
        AuthenticationModule::class,
        ServiceModule::class,
        AlarmModule::class,
        UseCaseModule::class,
        SplashModule::class,
        DirectionModule::class,
        HomeModule::class,
        HistoryModule::class,
        EditModule::class,
        InputModule::class,
        ReceiptModule::class,
        StatisticModule::class,
        DatePickerModule::class,
        TimelineModule::class,
        MoreModule::class,
        InviteModule::class,
        TicketModule::class,
        NotificationBuilder::class,
        PolicyModule::class,
        AskModule::class,
        NotificationModule::class,
        NoticeModule::class,
        ProfileModule::class,
        ChangeModule::class,
        ReplyModule::class,
        ContactModule::class,
        CopyModule::class,
        CodeModule::class,
    ]
)
interface ApplicationComponent : AndroidInjector<WoozooraApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<WoozooraApplication>
}
