package dev.kxxcn.woozoora.di

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.migration.Migration
import com.faltenreich.skeletonlayout.SkeletonConfig
import com.faltenreich.skeletonlayout.SkeletonLayout
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.WoozooraApplication
import dev.kxxcn.woozoora.common.base.BaseCoroutineScope
import dev.kxxcn.woozoora.common.base.BaseCoroutineScopeImpl
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.data.source.DataRepositoryImpl
import dev.kxxcn.woozoora.data.source.DataSource
import dev.kxxcn.woozoora.data.source.api.ApiFactory
import dev.kxxcn.woozoora.data.source.local.LocalDataSource
import dev.kxxcn.woozoora.data.source.local.LocalDatabase
import dev.kxxcn.woozoora.data.source.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDataSource

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideRemoteDataSource(): DataSource {
        return RemoteDataSource(ApiFactory.create())
    }

    @Singleton
    @LocalDataSource
    @Provides
    fun provideLocalDataSource(
        sharedPreferences: SharedPreferences,
        database: LocalDatabase,
        ioDispatcher: CoroutineDispatcher,
    ): DataSource {
        return LocalDataSource(
            sharedPreferences,
            database.userDao(),
            database.notificationDao(),
            ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideDataBase(application: WoozooraApplication): LocalDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            LocalDatabase::class.java,
            "Woozoora.db"
        ).addMigrations(*ALL_MIGRATIONS).fallbackToDestructiveMigrationOnDowngrade().build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideSharedPreference(application: WoozooraApplication): SharedPreferences {
        return application.getSharedPreferences(
            application.getString(R.string.app_name_en),
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideBaseCoroutineScope(): BaseCoroutineScope {
        return BaseCoroutineScopeImpl()
    }

    @Singleton
    @Provides
    fun provideNotificationManager(application: WoozooraApplication): NotificationManager {
        return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Singleton
    @Provides
    fun provideSkeletonConfig(application: WoozooraApplication): SkeletonConfig {
        return SkeletonConfig(
            application.color(R.color.shimmerBase),
            10.dpToPx.toFloat(),
            SkeletonLayout.DEFAULT_SHIMMER_SHOW,
            application.color(R.color.shimmerHighlight),
            SkeletonLayout.DEFAULT_SHIMMER_DURATION_IN_MILLIS,
            SkeletonLayout.DEFAULT_SHIMMER_DIRECTION,
            SkeletonLayout.DEFAULT_SHIMMER_ANGLE
        )
    }

    val ALL_MIGRATIONS = arrayOf<Migration>(

    )
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DataRepositoryImpl): DataRepository
}
