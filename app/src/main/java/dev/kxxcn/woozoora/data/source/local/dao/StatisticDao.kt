package dev.kxxcn.woozoora.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.kxxcn.woozoora.data.source.entity.StatisticEntity

@Dao
interface StatisticDao {

    @Query("SELECT * FROM Statistic WHERE date > :minimum ORDER BY date DESC")
    fun observeStatistics(minimum: Long): LiveData<List<StatisticEntity>>

    @Query("SELECT * FROM Statistic WHERE date > :minimum ORDER BY date DESC")
    suspend fun getStatistics(minimum: Long): List<StatisticEntity>

    @Query("SELECT * FROM Statistic")
    suspend fun getStatistics(): List<StatisticEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistic(statistic: StatisticEntity)

    @Update
    suspend fun updateStatistics(statistics: List<StatisticEntity>): Int
}
