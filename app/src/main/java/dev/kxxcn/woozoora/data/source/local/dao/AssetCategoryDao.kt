package dev.kxxcn.woozoora.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.kxxcn.woozoora.data.source.entity.AssetCategoryEntity

@Dao
interface AssetCategoryDao {

    @Query("SELECT * FROM acategory ORDER BY priority")
    fun observeCategories(): LiveData<List<AssetCategoryEntity>>

    @Query("SELECT * FROM acategory ORDER BY priority")
    suspend fun getAssetCategories(): List<AssetCategoryEntity>

    @Insert
    suspend fun insertCategory(category: AssetCategoryEntity)

    @Update
    suspend fun updateCategory(vararg category: AssetCategoryEntity)

    @Query("DELETE FROM acategory WHERE id in (:ids)")
    suspend fun deleteAll(ids: List<String>)
}
