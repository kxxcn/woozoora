package dev.kxxcn.woozoora.data.source

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.api.InvalidRequestException
import dev.kxxcn.woozoora.data.source.api.SendAskException
import dev.kxxcn.woozoora.data.source.entity.AssetCategoryEntity
import dev.kxxcn.woozoora.data.source.entity.TransactionCategoryEntity
import dev.kxxcn.woozoora.domain.model.*
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.util.Converter
import java.util.*
import java.util.concurrent.TimeUnit

class FakeRepository : DataRepository {

    private var usersData: LinkedHashMap<String, UserData> = LinkedHashMap()

    private var transactionsData: LinkedHashMap<Int, TransactionData> = LinkedHashMap()

    private var asksData: LinkedList<AskData> = LinkedList()

    private var notificationsData: LinkedList<NotificationData> = LinkedList()

    private var statisticsData: LinkedList<StatisticData> = LinkedList()

    override suspend fun getUser(userId: String?, dirtyCache: Boolean): Result<UserData> {
        return try {
            Result.Success(usersData[TEST_USER_ID]!!)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getGroup(exclude: Boolean): Result<List<UserData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOverview(): Result<OverviewData> {
        TODO("Not yet implemented")
    }

    override suspend fun getOverview(year: Int?, month: Int?): Result<OverviewData> {
        val (startDate, endDate) = Converter.rangeOfHomeFilterType(
            HomeFilterType.MONTHLY,
            year,
            month
        )

        val overview = OverviewData(
            usersData[TEST_USER_ID]!!,
            emptyList(),
            transactionsData.values.filter { it.date in startDate..endDate }
        )
        return Result.Success(overview)
    }

    override suspend fun getOverview(startDate: Long, endDate: Long): Result<OverviewData> {
        return Result.Success(OverviewData(usersData[TEST_USER_ID]!!, emptyList(), emptyList()))
    }

    override suspend fun getNotificationOption(option: OptionData): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getNotice(): Result<List<NoticeData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAsks(): Result<List<AskData>> {
        return Result.Success(asksData)
    }

    override suspend fun getUsageTransactionTime(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAssetCategory(): Result<List<AssetCategoryData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionCategory(): Result<List<TransactionCategoryData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getStatistics(): Result<List<StatisticData>> {
        val date = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        return Result.Success(statisticsData.filter { it.date > date })
    }

    override fun getNotifications(): LiveData<List<NotificationData>> {
        val date = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        return MutableLiveData(notificationsData.filter { (it.transactionDate ?: 0) > date })
    }

    override suspend fun saveUser(userData: UserData): Result<Any> {
        return Result.Success(Unit)
    }

    override suspend fun saveToken(newToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTransaction(transactionData: TransactionData): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNotificationOption(
        option: OptionData,
        value: Boolean,
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNotification(notification: NotificationData) {
        TODO("Not yet implemented")
    }

    override suspend fun saveStatistic(statistic: StatisticData) {
        TODO("Not yet implemented")
    }

    override suspend fun saveUsageTransactionTime(value: Boolean): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTransactionCategory(category: String): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAssetCategory(category: String): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun updateToken() {
        Result.Success(Unit)
    }

    override suspend fun updateUser(sponsorId: String, isTransfer: Boolean): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(year: Int): Result<Any> {
        return if (year == 1000) Result.Error(InvalidRequestException()) else Result.Success(Unit)
    }

    override suspend fun updateUser(budget: Long): Result<Any> {
        return if (budget == -1L) Result.Error(InvalidRequestException()) else Result.Success(Unit)
    }

    override suspend fun updateNotification() {
        val updateData = notificationsData.map { it.copy(isChecked = true) }
        notificationsData.clear()
        notificationsData.addAll(updateData)
    }

    override suspend fun updateStatistic() {
        val updateData = statisticsData.map { it.copy(isChecked = true) }
        statisticsData.clear()
        statisticsData.addAll(updateData)
    }

    override suspend fun updateCode(code: String, isTransfer: Boolean): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTransactionCategory(list: List<TransactionCategoryEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAssetCategory(list: List<AssetCategoryEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: TransactionData?): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransactionCategory(ids: List<Int>): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAssetCategory(ids: List<String>): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun sendAsk(ask: AskData): Result<Any> {
        return if (usersData.isEmpty()) {
            Result.Error(SendAskException())
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun leave(): Result<Any> {
        TODO("Not yet implemented")
    }

    override fun observeTransactionCategory(): LiveData<List<TransactionCategoryData>> {
        TODO("Not yet implemented")
    }

    override fun observeAssetCategory(): LiveData<List<AssetCategoryData>> {
        TODO("Not yet implemented")
    }

    override fun observeStatistics(): LiveData<List<StatisticData>> {
        TODO("Not yet implemented")
    }

    @VisibleForTesting
    fun addUser(user: UserData) {
        usersData[user.id] = user
    }

    @VisibleForTesting
    fun addAsks(vararg asks: AskData) {
        for (ask in asks) {
            asksData.add(ask)
        }
    }

    @VisibleForTesting
    fun addTransactions(vararg transactions: TransactionData) {
        for (transaction in transactions) {
            transactionsData[transaction.id] = transaction
        }
    }

    @VisibleForTesting
    fun addNotifications(vararg notifications: NotificationData) {
        for (notification in notifications) {
            notificationsData.add(notification)
        }
    }

    @VisibleForTesting
    fun addStatistics(vararg statistics: StatisticData) {
        for (statistic in statistics) {
            statisticsData.add(statistic)
        }
    }

    @VisibleForTesting
    fun clearUser() {
        usersData.clear()
    }
}
