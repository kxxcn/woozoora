package dev.kxxcn.woozoora.di

import dagger.Module
import dagger.Provides
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.*
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Singleton
    @Provides
    fun provideGetUserUseCase(repository: DataRepository): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetGroupUseCase(repository: DataRepository): GetGroupUseCase {
        return GetGroupUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetOverviewUseCase(repository: DataRepository): GetOverviewUseCase {
        return GetOverviewUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetNotificationOptionUseCase(repository: DataRepository): GetNotificationOptionUseCase {
        return GetNotificationOptionUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetNotificationsUseCase(repository: DataRepository): GetNotificationsUseCase {
        return GetNotificationsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetNoticeUseCase(repository: DataRepository): GetNoticeUseCase {
        return GetNoticeUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAsksUseCase(repository: DataRepository): GetAsksUseCase {
        return GetAsksUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSaveUserUseCase(repository: DataRepository): SaveUserUseCase {
        return SaveUserUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSaveTransactionUseCase(repository: DataRepository): SaveTransactionUseCase {
        return SaveTransactionUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSaveTokenUseCase(repository: DataRepository): SaveTokenUseCase {
        return SaveTokenUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSaveNotificationOptionUseCase(repository: DataRepository): SaveNotificationOptionUseCase {
        return SaveNotificationOptionUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSaveNotificationUseCase(repository: DataRepository): SaveNotificationUseCase {
        return SaveNotificationUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateTokenUseCase(repository: DataRepository): UpdateTokenUseCase {
        return UpdateTokenUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateUserUseCase(repository: DataRepository): UpdateUserUseCase {
        return UpdateUserUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateNotification(repository: DataRepository): UpdateNotificationUseCase {
        return UpdateNotificationUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteTransactionUseCase(repository: DataRepository): DeleteTransactionUseCase {
        return DeleteTransactionUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSendAskUseCase(repository: DataRepository): SendAskUseCase {
        return SendAskUseCase(repository)
    }
}
