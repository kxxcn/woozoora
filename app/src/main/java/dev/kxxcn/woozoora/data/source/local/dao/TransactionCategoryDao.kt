package dev.kxxcn.woozoora.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.kxxcn.woozoora.data.source.entity.TransactionCategoryEntity

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM tcategory")
    suspend fun getTransactionCategories(): List<TransactionCategoryEntity>
}
