package dev.kxxcn.woozoora.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.messaging.FirebaseMessaging
import dev.kxxcn.woozoora.common.extension.toData
import dev.kxxcn.woozoora.common.extension.toEntity
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.data.source.api.InvalidRequestException
import dev.kxxcn.woozoora.data.source.api.NoResultException
import dev.kxxcn.woozoora.data.source.api.UnverifiedUserException
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
        return when (val result = dataSource.getUser(userId)) {
            is Result.Success -> {
                val userEntity = result.data.also {
                    if (dirtyCache) {
                        localDataSource.saveUser(it)
                    }
                }
                Result.Success(userEntity.toData())
            }
            is Result.Error -> Result.Error(result.exception)
            else -> Result.Error(UnverifiedUserException())
        }
    }

    override suspend fun getGroup(exclude: Boolean): Result<List<UserData>> {
        val cache = localDataSource.getUser(null)
        return if (cache is Result.Success) {
            return when (val result = remoteDataSource.getGroup(cache.data.id, exclude)) {
                is Result.Success -> Result.Success(result.data.map { it.toData() })
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(InvalidRequestException())
            }
        } else {
            Result.Error(UnverifiedUserException())
        }
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
        return when (val result = remoteDataSource.getNotice()) {
            is Result.Success -> Result.Success(result.data.map { it.toData() })
            is Result.Error -> Result.Error(result.exception)
            else -> Result.Error(InvalidRequestException())
        }
    }

    override suspend fun getAsks(): Result<List<AskData>> {
        return when (val result = remoteDataSource.getAsks()) {
            is Result.Success -> Result.Success(
                result.data
                    .map { it.toData() }
                    .sortedByDescending { it.date }
            )
            is Result.Error -> Result.Error(result.exception)
            else -> Result.Error(InvalidRequestException())
        }
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
        val cache = localDataSource.getUser(null)
        if (cache is Result.Success) {
            val token = localDataSource.getToken() ?: getNewToken()
            if (cache.data.token != token) {
                remoteDataSource.updateToken(cache.data.id, token)
            }
        }
    }

    override suspend fun updateUser(sponsorId: String, isTransfer: Boolean): Result<String?> {
        val cache = localDataSource.getUser(null)
        return if (cache is Result.Success) {
            val user = cache.data
            remoteDataSource
                .updateUser(user.id, sponsorId, isTransfer)
                .ifSucceeded { localDataSource.updateCode(it) }
        } else {
            Result.Error(UnverifiedUserException())
        }
    }

    override suspend fun updateUser(year: Int): Result<Any> {
        val cache = localDataSource.getUser(null)
        return if (cache is Result.Success) {
            val user = cache.data
            remoteDataSource
                .updateUser(user.id, year)
                .ifSucceeded { localDataSource.updateUser(user.id, year) }
        } else {
            Result.Error(UnverifiedUserException())
        }
    }

    override suspend fun updateUser(budget: Long): Result<Any> {
        val cache = localDataSource.getUser(null)
        return if (cache is Result.Success) {
            val user = cache.data
            remoteDataSource
                .updateUser(user.id, budget)
                .ifSucceeded { localDataSource.updateUser(user.id, budget) }
        } else {
            Result.Error(UnverifiedUserException())
        }
    }

    override suspend fun updateNotification() {
        localDataSource.updateNotification()
    }

    override suspend fun deleteTransaction(transaction: TransactionData?): Result<Any> {
        return remoteDataSource.deleteTransaction(transaction?.toEntity())
    }

    override suspend fun sendAsk(ask: AskData): Result<Any> {
        val cache = localDataSource.getUser(null)
        return if (cache is Result.Success) {
            remoteDataSource.sendAsk(cache.data.id, ask.toEntity())
        } else {
            Result.Error(UnverifiedUserException())
        }
    }

    override suspend fun leave(): Result<Any> {
        val cache = localDataSource.getUser(null)
        return if (cache is Result.Success) {
            val userId = cache.data.id
            remoteDataSource
                .leave(userId)
                .ifSucceeded { localDataSource.leave(userId) }
        } else {
            Result.Error(UnverifiedUserException())
        }
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
        val cache = localDataSource.getUser(null)
        return if (cache is Result.Success) {
            val (startDate, endDate) = Converter.rangeOfHomeFilterType(
                HomeFilterType.MONTHLY,
                year,
                month
            )
            when (val result = remoteDataSource.getOverview(cache.data.id, startDate, endDate)) {
                is Result.Success -> Result.Success(result.data.toData(cache.data))
                else -> Result.Error(NoResultException())
            }
        } else {
            Result.Error(UnverifiedUserException())
        }
    }
}
