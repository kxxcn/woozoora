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
    fun provideGetStatisticUseCase(repository: DataRepository): GetStatisticUseCase {
        return GetStatisticUseCase(repository)
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
    fun provideGetAssetCategoryUseCase(repository: DataRepository): GetAssetCategoryUseCase {
        return GetAssetCategoryUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetTransactionCategoryUseCase(repository: DataRepository): GetTransactionCategoryUseCase {
        return GetTransactionCategoryUseCase(repository)
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
    fun provideSaveStatisticUseCase(repository: DataRepository): SaveStatisticUseCase {
        return SaveStatisticUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSaveEditAdsEnabledCountUseCase(repository: DataRepository): SaveEditAdsEnabledCountUseCase {
        return SaveEditAdsEnabledCountUseCase(repository)
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
    fun provideUpdateNotificationUseCase(repository: DataRepository): UpdateNotificationUseCase {
        return UpdateNotificationUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateStatisticUseCase(repository: DataRepository): UpdateStatisticUseCase {
        return UpdateStatisticUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteTransactionUseCase(repository: DataRepository): DeleteTransactionUseCase {
        return DeleteTransactionUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteTransactionCategoryUseCase(repository: DataRepository): DeleteTransactionCategoryUseCase {
        return DeleteTransactionCategoryUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteAssetCategoryUseCase(repository: DataRepository): DeleteAssetCategoryUseCase {
        return DeleteAssetCategoryUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSendAskUseCase(repository: DataRepository): SendAskUseCase {
        return SendAskUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideIsEnableEditAdsUseCase(repository: DataRepository): IsEnableEditAdsUseCase {
        return IsEnableEditAdsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideObserveTransactionCategoryUseCase(repository: DataRepository): ObserveTransactionCategoryUseCase {
        return ObserveTransactionCategoryUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideObserveAssetCategoryUseCase(repository: DataRepository): ObserveAssetCategoryUseCase {
        return ObserveAssetCategoryUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideObserveStatisticUseCase(repository: DataRepository): ObserveStatisticUseCase {
        return ObserveStatisticUseCase(repository)
    }
}
