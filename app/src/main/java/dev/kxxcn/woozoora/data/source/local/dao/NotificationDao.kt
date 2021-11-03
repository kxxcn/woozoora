package dev.kxxcn.woozoora.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.kxxcn.woozoora.data.source.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Query("SELECT * FROM Notification WHERE transactionDate > :minimum ORDER BY transactionDate DESC")
    fun observeNotifications(minimum: Long): LiveData<List<NotificationEntity>>

    @Query("SELECT * FROM Notification")
    suspend fun getNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM Notification WHERE transactionId=:id LIMIT 1")
    suspend fun findNotification(id: String?): NotificationEntity?

    @Query("DELETE FROM Notification")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Update
    suspend fun updateNotifications(notifications: List<NotificationEntity>): Int

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
}
