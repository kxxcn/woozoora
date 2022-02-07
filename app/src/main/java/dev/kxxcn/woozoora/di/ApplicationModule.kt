package dev.kxxcn.woozoora.di

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faltenreich.skeletonlayout.SkeletonConfig
import com.faltenreich.skeletonlayout.SkeletonLayout
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.WoozooraApplication
import dev.kxxcn.woozoora.common.base.BaseCoroutineScope
import dev.kxxcn.woozoora.common.base.BaseCoroutineScopeImpl
import dev.kxxcn.woozoora.common.base.BillingProvider
import dev.kxxcn.woozoora.common.base.BillingProviderImpl
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
import java.util.*
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
            database.assetCategoryDao(),
            database.transactionCategoryDao(),
            database.statisticDao(),
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
        ).fallbackToDestructiveMigrationOnDowngrade()
            .addCallback(CALLBACK)
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideContext(application: WoozooraApplication): Context = application.applicationContext

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

    private val ASSET_CATEGORY = listOf(
        "월급",
        "예금",
        "적금",
        "부동산",
        "가상화폐",
        "주식",
        "펀드",
        "현금"
    )
    private val TRANSACTION_CATEGORY = listOf(
        "식비",
        "교통/차량",
        "문화생활",
        "마트/편의점",
        "패션/미용",
        "생활용품",
        "주거/통신",
        "건강",
        "교육",
        "경조사/회비",
        "부모님",
        "기타",
        "카페",
        "육아",
        "의료",
        "대출",
        "보험",
    )

    val CALLBACK = object : RoomDatabase.Callback() {
        override fun onCreate(database: SupportSQLiteDatabase) {
            super.onCreate(database)
            insertAssetCategories(database)
            insertTransactionCategories(database)
        }
    }

    val ALL_MIGRATIONS = arrayOf(
        object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                createAssetCategoryTable(database)
                createTransactionCategoryTable(database)
                insertAssetCategories(database)
                insertTransactionCategories(database)
                alterNotificationTable(database)
            }
        },
        object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                createStatisticTable(database)
            }
        }
    )

    private fun createAssetCategoryTable(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `ACategory` (`id` TEXT NOT NULL, `category` TEXT NOT NULL, `priority` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }

    private fun createTransactionCategoryTable(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `TCategory` (`id` INTEGER NOT NULL, `category` TEXT NOT NULL, `priority` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }

    private fun createStatisticTable(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `Statistic` (`id` TEXT NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `date` INTEGER NOT NULL, `isChecked` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }

    private fun insertAssetCategories(database: SupportSQLiteDatabase) {
        ASSET_CATEGORY.forEachIndexed { index, category ->
            database.execSQL("INSERT INTO `ACategory`(id, category, priority) VALUES('${UUID.randomUUID()}', '${category}', ${index})")
        }
    }

    private fun insertTransactionCategories(database: SupportSQLiteDatabase) {
        TRANSACTION_CATEGORY.forEachIndexed { index, category ->
            database.execSQL("INSERT INTO `TCategory`(id, category, priority) VALUES(${index}, '${category}', ${index})")
        }
    }

    private fun alterNotificationTable(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `Notification` ADD COLUMN transactionType INTEGER DEFAULT 0")
    }
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DataRepositoryImpl): DataRepository

    @Binds
    abstract fun bindProvider(billing: BillingProviderImpl): BillingProvider
}
