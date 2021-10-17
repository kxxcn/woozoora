package dev.kxxcn.woozoora.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.kxxcn.woozoora.data.source.entity.TransactionCategoryEntity

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM tcategory ORDER BY priority")
    fun observeCategories(): LiveData<List<TransactionCategoryEntity>>

    @Query("SELECT * FROM tcategory ORDER BY priority")
    suspend fun getTransactionCategories(): List<TransactionCategoryEntity>

    @Insert
    suspend fun insertCategory(category: TransactionCategoryEntity)

    @Update
    suspend fun updateCategory(vararg category: TransactionCategoryEntity)

    @Query("DELETE FROM tcategory WHERE id in (:ids)")
    suspend fun deleteAll(ids: List<Int>)
}
