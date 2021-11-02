package dev.kxxcn.woozoora.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kxxcn.woozoora.common.ROOM_VERSION
import dev.kxxcn.woozoora.data.source.entity.*
import dev.kxxcn.woozoora.data.source.local.dao.*

@Database(
    entities = [
        UserEntity::class,
        NotificationEntity::class,
        AssetCategoryEntity::class,
        TransactionCategoryEntity::class,
        StatisticEntity::class,
    ],
    version = ROOM_VERSION
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun notificationDao(): NotificationDao

    abstract fun assetCategoryDao(): AssetCategoryDao

    abstract fun transactionCategoryDao(): TransactionCategoryDao

    abstract fun statisticDao(): StatisticDao
}
