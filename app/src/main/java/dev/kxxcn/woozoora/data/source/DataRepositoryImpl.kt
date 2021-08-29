package dev.kxxcn.woozoora.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.messaging.FirebaseMessaging
import dev.kxxcn.woozoora.common.extension.toData
import dev.kxxcn.woozoora.common.extension.toEntity
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.flatMap
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.data.map
import dev.kxxcn.woozoora.di.ApplicationModule
import dev.kxxcn.woozoora.domain.model.*
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.util.Converter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DataRepositoryImpl @Inject constructor(
    @ApplicationModule.LocalDataSource private val localDataSource: DataSource,
    @ApplicationModule.RemoteDataSource private val remoteDataSource: DataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DataRepository {

    override suspend fun getUser(userId: String?, dirtyCache: Boolean): Result<UserData> {
        val dataSource = if (dirtyCache) remoteDataSource else localDataSource
        return dataSource.getUser(userId)
            .map {
                if (dirtyCache) localDataSource.saveUser(it)
                it.toData()
            }
    }

    override suspend fun getGroup(exclude: Boolean): Result<List<UserData>> {
        return localDataSource.getUser(null)
            .flatMap { remoteDataSource.getGroup(it.id, exclude) }
            .map { group -> group.map { it.toData() } }
    }

    override suspend fun getOverview(): Result<OverviewData> {
        return getOverviewOptionally()
    }

    override suspend fun getOverview(year: Int?, month: Int?): Result<OverviewData> {
        return getOverviewOptionally(year, month)
    }

    override suspend fun getNotificationOption(option: OptionData): Boolean {
        return localDataSource.getNotificationOption(option.toEntity())
    }

    override suspend fun getNotice(): Result<List<NoticeData>> {
        return remoteDataSource.getNotice()
            .map { notices -> notices.map { it.toData() } }
    }

    override suspend fun getAsks(): Result<List<AskData>> {
        return remoteDataSource.getAsks()
            .map { asks -> asks.map { it.toData() }.sortedByDescending { it.date } }
    }

    override suspend fun getUsageTransactionTime(): Boolean {
        return localDataSource.getUsageTransactionTime()
    }

    override fun getNotifications(): LiveData<List<NotificationData>> {
        return MediatorLiveData<List<NotificationData>>().apply {
            addSource(localDataSource.getNotifications()) {
                value = it.map { entity -> entity.toData() }
            }
        }
    }

    override suspend fun saveUser(userData: UserData): Result<Any> {
        val userEntity = userData
            .apply { this.token = getNewToken() }
            .toEntity()

        return remoteDataSource
            .saveUser(userEntity)
            .ifSucceeded { localDataSource.saveUser(userEntity) }
    }

    override suspend fun saveToken(newToken: String) {
        localDataSource.saveToken(newToken)
    }

    override suspend fun saveTransaction(transactionData: TransactionData): Result<Any> {
        return remoteDataSource.saveTransaction(transactionData.toEntity())
    }

    override suspend fun saveNotificationOption(
        option: OptionData,
        value: Boolean,
    ): Result<Boolean> {
        return localDataSource.saveNotificationOption(option.toEntity(), value)
    }

    override suspend fun saveNotification(notification: NotificationData) {
        localDataSource.saveNotification(notification.toEntity())
    }

    override suspend fun saveUsageTransactionTime(value: Boolean): Result<Boolean> {
        return localDataSource.saveUsageTransactionTime(value)
    }

    override suspend fun updateToken() {
        localDataSource.getUser(null)
            .ifSucceeded {
                val token = localDataSource.getToken() ?: getNewToken()
                if (it.token != token) {
                    remoteDataSource.updateToken(it.id, token)
                }
            }
    }

    override suspend fun updateUser(sponsorId: String, isTransfer: Boolean): Result<Any> {
        return localDataSource.getUser(null)
            .flatMap { remoteDataSource.updateUser(it.id, sponsorId, isTransfer) }
            .flatMap { localDataSource.updateCode(it.userId, it.code, isTransfer) }
    }

    override suspend fun updateUser(year: Int): Result<Any> {
        return localDataSource.getUser(null)
            .flatMap { remoteDataSource.updateUser(it.id, year) }
            .flatMap { localDataSource.updateUser(it, year) }
    }

    override suspend fun updateUser(budget: Long): Result<Any> {
        return localDataSource.getUser(null)
            .flatMap { remoteDataSource.updateUser(it.id, budget) }
            .flatMap { localDataSource.updateUser(it, budget) }
    }

    override suspend fun updateNotification() {
        localDataSource.updateNotification()
    }

    override suspend fun updateCode(code: String, isTransfer: Boolean): Result<Any> {
        return localDataSource.getUser(null)
            .flatMap { remoteDataSource.updateCode(it.id, code, isTransfer) }
            .flatMap { localDataSource.updateCode(it, code, isTransfer) }
    }

    override suspend fun deleteTransaction(transaction: TransactionData?): Result<Any> {
        return remoteDataSource.deleteTransaction(transaction?.toEntity())
    }

    override suspend fun sendAsk(ask: AskData): Result<Any> {
        return localDataSource.getUser(null)
            .flatMap { remoteDataSource.sendAsk(it.id, ask.toEntity()) }
    }

    override suspend fun leave(): Result<Any> {
        return localDataSource.getUser(null)
            .flatMap { remoteDataSource.leave(it.id) }
            .flatMap { localDataSource.leave(it) }
    }

    private suspend fun getNewToken() = suspendCoroutine<String?> { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(it.result)
            } else {
                continuation.resume(null)
            }
        }
    }

    private suspend fun getOverviewOptionally(
        year: Int? = null,
        month: Int? = null,
    ): Result<OverviewData> {
        return localDataSource.getUser(null)
            .flatMap { cache ->
                val (startDate, endDate) = Converter.rangeOfHomeFilterType(
                    HomeFilterType.MONTHLY,
                    year,
                    month
                )
                remoteDataSource
                    .getOverview(cache.id, startDate, endDate)
                    .map { it.toData(cache) }
            }
    }
}
