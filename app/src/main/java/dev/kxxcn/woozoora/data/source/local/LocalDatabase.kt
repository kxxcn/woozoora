package dev.kxxcn.woozoora.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kxxcn.woozoora.common.ROOM_VERSION
import dev.kxxcn.woozoora.data.source.entity.AssetCategoryEntity
import dev.kxxcn.woozoora.data.source.entity.NotificationEntity
import dev.kxxcn.woozoora.data.source.entity.TransactionCategoryEntity
import dev.kxxcn.woozoora.data.source.entity.UserEntity
import dev.kxxcn.woozoora.data.source.local.dao.AssetCategoryDao
import dev.kxxcn.woozoora.data.source.local.dao.NotificationDao
import dev.kxxcn.woozoora.data.source.local.dao.TransactionCategoryDao
import dev.kxxcn.woozoora.data.source.local.dao.UserDao

@Database(
    entities = [
        UserEntity::class,
        NotificationEntity::class,
        AssetCategoryEntity::class,
        TransactionCategoryEntity::class,
    ],
    version = ROOM_VERSION
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun notificationDao(): NotificationDao

    abstract fun assetCategoryDao(): AssetCategoryDao

    abstract fun transactionCategoryDao(): TransactionCategoryDao
}
