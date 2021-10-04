package dev.kxxcn.woozoora.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.kxxcn.woozoora.data.source.entity.AssetCategoryEntity

@Dao
interface AssetCategoryDao {

    @Query("SELECT * FROM acategory")
    suspend fun getAssetCategories(): List<AssetCategoryEntity>
}
