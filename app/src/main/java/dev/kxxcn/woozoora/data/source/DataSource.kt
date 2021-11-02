package dev.kxxcn.woozoora.data.source

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.entity.*

interface DataSource {

    suspend fun getUser(userId: String?): Result<UserEntity>

    suspend fun getGroup(userId: String, exclude: Boolean): Result<List<UserEntity>>

    suspend fun getToken(): String?

    suspend fun getTransactions(
        userId: String,
        startDate: Long,
        endDate: Long,
    ): Result<List<TransactionEntity>>

    suspend fun getOverview(
        userId: String?,
        startDate: Long,
        endDate: Long,
    ): Result<OverviewEntity>

    suspend fun getNotificationOption(option: OptionEntity): Boolean

    suspend fun getNotice(): Result<List<NoticeEntity>>

    suspend fun getAsks(): Result<List<AskEntity>>

    suspend fun getUsageTransactionTime(): Boolean

    suspend fun getAssetCategory(): Result<List<AssetCategoryEntity>>

    suspend fun getTransactionCategory(): Result<List<TransactionCategoryEntity>>

    suspend fun getStatistics(): Result<List<StatisticEntity>>

    fun getNotifications(): LiveData<List<NotificationEntity>>

    suspend fun saveUser(user: UserEntity): Result<String?>

    suspend fun saveToken(newToken: String)

    suspend fun saveTransaction(transaction: TransactionEntity): Result<Any>

    suspend fun saveNotificationOption(
        option: OptionEntity,
        value: Boolean,
    ): Result<Boolean>

    suspend fun saveNotification(notification: NotificationEntity)

    suspend fun saveStatistic(statistic: StatisticEntity)

    suspend fun saveUsageTransactionTime(value: Boolean): Result<Boolean>

    suspend fun saveTransactionCategory(category: String): Result<Any>

    suspend fun saveAssetCategory(category: String): Result<Any>

    suspend fun updateToken(userId: String, token: String?)

    suspend fun updateUser(
        userId: String,
        sponsorId: String,
        isTransfer: Boolean,
    ): Result<CodeEntity>

    suspend fun updateUser(userId: String, year: Int): Result<String>

    suspend fun updateUser(userId: String, budget: Long): Result<String>

    suspend fun updateCode(userId: String, code: String?, isTransfer: Boolean): Result<String>

    suspend fun updateTransactionCategory(list: List<TransactionCategoryEntity>)

    suspend fun updateAssetCategory(list: List<AssetCategoryEntity>)

    suspend fun updateNotification()

    suspend fun updateStatistic()

    suspend fun deleteTransaction(transaction: TransactionEntity?): Result<Any>

    suspend fun deleteTransactionCategory(ids: List<Int>): Result<Int>

    suspend fun deleteAssetCategory(ids: List<String>): Result<Int>

    suspend fun sendAsk(userId: String, ask: AskEntity): Result<Any>

    suspend fun leave(userId: String): Result<String>

    fun observeTransactionCategory(): LiveData<List<TransactionCategoryEntity>>

    fun observeAssetCategory(): LiveData<List<AssetCategoryEntity>>
}
