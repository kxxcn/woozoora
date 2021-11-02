package dev.kxxcn.woozoora.data.source

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.entity.AssetCategoryEntity
import dev.kxxcn.woozoora.data.source.entity.TransactionCategoryEntity
import dev.kxxcn.woozoora.domain.model.*

interface DataRepository {

    suspend fun getUser(userId: String?, dirtyCache: Boolean): Result<UserData>

    suspend fun getGroup(exclude: Boolean): Result<List<UserData>>

    suspend fun getOverview(): Result<OverviewData>

    suspend fun getOverview(year: Int?, month: Int?): Result<OverviewData>

    suspend fun getOverview(startDate: Long, endDate: Long): Result<OverviewData>

    suspend fun getNotificationOption(option: OptionData): Boolean

    suspend fun getNotice(): Result<List<NoticeData>>

    suspend fun getAsks(): Result<List<AskData>>

    suspend fun getUsageTransactionTime(): Boolean

    suspend fun getAssetCategory(): Result<List<AssetCategoryData>>

    suspend fun getTransactionCategory(): Result<List<TransactionCategoryData>>

    suspend fun getStatistics(): Result<List<StatisticData>>

    fun getNotifications(): LiveData<List<NotificationData>>

    suspend fun saveUser(userData: UserData): Result<Any?>

    suspend fun saveToken(newToken: String)

    suspend fun saveTransaction(transactionData: TransactionData): Result<Any>

    suspend fun saveNotificationOption(
        option: OptionData,
        value: Boolean,
    ): Result<Boolean>

    suspend fun saveNotification(notification: NotificationData)

    suspend fun saveStatistic(statistic: StatisticData)

    suspend fun saveUsageTransactionTime(value: Boolean): Result<Boolean>

    suspend fun saveTransactionCategory(category: String): Result<Any>

    suspend fun saveAssetCategory(category: String): Result<Any>

    suspend fun updateToken()

    suspend fun updateUser(sponsorId: String, isTransfer: Boolean): Result<Any>

    suspend fun updateUser(year: Int): Result<Any>

    suspend fun updateUser(budget: Long): Result<Any>

    suspend fun updateNotification()

    suspend fun updateStatistic()

    suspend fun updateTransactionCategory(list: List<TransactionCategoryEntity>)

    suspend fun updateAssetCategory(list: List<AssetCategoryEntity>)

    suspend fun updateCode(code: String, isTransfer: Boolean): Result<Any>

    suspend fun deleteTransaction(transaction: TransactionData?): Result<Any>

    suspend fun deleteTransactionCategory(ids: List<Int>): Result<Int>

    suspend fun deleteAssetCategory(ids: List<String>): Result<Int>

    suspend fun sendAsk(ask: AskData): Result<Any>

    suspend fun leave(): Result<Any>

    fun observeTransactionCategory(): LiveData<List<TransactionCategoryData>>

    fun observeAssetCategory(): LiveData<List<AssetCategoryData>>
}
