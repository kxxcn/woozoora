package dev.kxxcn.woozoora.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.kxxcn.woozoora.data.source.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun observeUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM User")
    suspend fun getUsers(): List<UserEntity>

    @Query("DELETE FROM User")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity): Int

    @Delete
    suspend fun deleteUser(user: UserEntity)
}
