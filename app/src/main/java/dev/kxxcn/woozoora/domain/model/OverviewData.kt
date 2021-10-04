package dev.kxxcn.woozoora.domain.model

import dev.kxxcn.woozoora.common.Payment
import dev.kxxcn.woozoora.common.extension.day
import dev.kxxcn.woozoora.common.extension.weekday
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.util.Converter

data class OverviewData(
    val user: UserData,
    val group: List<UserData>,
    val transactions: List<TransactionData>,
) {

    val id: String
        get() = user.id

    val name: String?
        get() = user.name

    val profile: String?
        get() = user.profile

    val budget: Long
        get() = user.budget

    val allUsers: List<UserData>
        get() = listOf(user) + group

    val hasGroup: Boolean
        get() = group.isNotEmpty()

    val hasTransactions: Boolean
        get() = transactions.isNotEmpty()

    val totalCountOfTransactions: Int
        get() = transactions.size

    fun getTotalSpendingById(userId: String?): Int {
        return filterTransactionToId(userId).sumBy { it.price }
    }

    fun getTotalSpendingForDaily(userId: String?, day: Int): Int {
        return filterTransactionToDay(userId, day).sumBy { it.price }
    }

    @JvmOverloads
    fun getTotalSpendingByType(
        userId: String? = null,
        filterType: HomeFilterType,
        year: Int? = null,
        month: Int? = null,
    ): Int {
        return filterTransactionToType(
            userId,
            filterType,
            year,
            month
        ).sumBy { it.price }
    }

    fun getTotalSpendingOfMostSpent(filterType: HomeFilterType): String {
        return getTotalSpendingToMostSpent(filterType).toString()
    }

    fun getTotalSpendingByRange(userId: String? = null, range: Pair<Long, Long>?): Int {
        return range
            ?.let { pair -> filterTransactionToRange(userId, pair).sumBy { it.price } }
            ?: 0
    }

    fun getTotalSpendingByPayment(userId: String? = null, payment: Payment): Int {
        return filterTransactionToId(userId)
            .filter { it.payment == payment.ordinal }
            .sumBy { it.price }
    }

    fun getTotalAssetById(userId: String?): Int {
        return filterTransactionToId(userId, 1).sumBy { it.price }
    }

    fun groupByCategory(
        userId: String? = null,
        filterType: HomeFilterType,
        year: Int? = null,
        month: Int? = null,
    ): Map<String?, List<TransactionData>> {
        return filterTransactionToType(userId, filterType, year, month)
            .groupBy { it.category }
            .mapKeys { it.value.firstOrNull()?.domain }
    }

    fun getResourceOfCategory(userId: String?, day: Int): List<String?> {
        return filterTransactionToDay(userId, day).map { it.domain }
    }

    fun getRatioByPayment(
        userId: String? = null,
        filterType: HomeFilterType,
    ): List<Pair<Payment, Float>> {
        return groupByPayment(userId, filterType).map { (payment, transactions) ->
            val ratio = transactions.size.toFloat() / filterTransactionToType(
                userId,
                filterType
            ).size.toFloat()
            payment to ratio
        }
    }

    fun calculateUsageRateByCategory(
        userId: String? = null,
        filterType: HomeFilterType,
        transactionsByCategory: List<TransactionData>,
        year: Int? = null,
        month: Int? = null,
    ): Float {
        return transactionsByCategory.sumBy { it.price }.toFloat() / filterTransactionToType(
            userId,
            filterType,
            year,
            month
        ).sumBy { it.price }.toFloat()
    }

    fun getUsageRateOfBudget(
        userId: String?,
        filterType: HomeFilterType? = null,
        range: Pair<Long, Long>? = null,
        year: Int? = null,
        month: Int? = null,
    ): Float {
        val totalSpending = filterType
            ?.let { getTotalSpendingByType(userId, it, year, month) }
            ?: getTotalSpendingByRange(userId, range)

        return calculateUsageRateOfBudget(totalSpending)
    }

    fun getUsageRateOfBudgetByUser(userId: String, filterType: HomeFilterType): Int {
        val totalSpending = getTotalSpendingByType(filterType = filterType).toFloat()
        val userSpending = getTotalSpendingByType(userId, filterType).toFloat()
        return userSpending
            .takeIf { it > 0f }
            ?.let { (it / totalSpending * 100).toInt() }
            ?: 0
    }

    fun getProfileOfMostSpent(filterType: HomeFilterType): String? {
        return getUserAndTransactionsToMostSpent(filterType)?.key
    }

    fun groupByWeekday(
        userId: String? = null,
        filterType: HomeFilterType,
    ): Map<Int, List<TransactionData>> {
        return filterTransactionToType(userId, filterType).groupBy { it.date.weekday }
    }

    fun getCountOfMostSpentCategory(): Int {
        return groupByCategory(filterType = HomeFilterType.MONTHLY)
            .maxOfOrNull { it.value.size }
            ?: 0
    }

    fun getMostSpentCategory(): String? {
        return groupByCategory(filterType = HomeFilterType.MONTHLY)
            .maxWithOrNull { o1, o2 -> o1.value.size.compareTo(o2.value.size) }
            ?.key
    }

    fun getCountOfTransactionsById(userId: String?): Int {
        return filterTransactionToId(userId).size
    }

    fun filterTransactionToId(userId: String?, branch: Int = 0): List<TransactionData> {
        val predicate = if (userId == null) {
            { true }
        } else {
            { t: TransactionData -> t.userId == userId }
        }
        return transactions.filter(predicate).filter { it.type == branch }
    }

    fun filterTransactionToRange(
        userId: String? = null,
        range: Pair<Long, Long>,
    ): List<TransactionData> {
        val (startDate, endDate) = range
        return filterTransactionToId(userId).filter { it.date in startDate..endDate }
    }

    private fun groupByPayment(
        userId: String? = null,
        filterType: HomeFilterType,
    ): Map<Payment, List<TransactionData>> {
        return filterTransactionToType(userId, filterType)
            .groupBy { it.payment }
            .mapKeys { Payment.find(it.key) }
    }

    private fun filterTransactionToDay(userId: String?, day: Int): List<TransactionData> {
        return filterTransactionToId(userId).filter { it.date.day == day }
    }

    private fun filterTransactionToType(
        userId: String? = null,
        filterType: HomeFilterType,
        year: Int? = null,
        month: Int? = null,
    ): List<TransactionData> {
        return filterTransactionToRange(
            userId,
            Converter.rangeOfHomeFilterType(filterType, year, month)
        )
    }

    private fun getUserAndTransactionsToMostSpent(filterType: HomeFilterType): Map.Entry<String?, List<TransactionData>>? {
        return filterTransactionToType(filterType = filterType)
            .groupBy { transaction -> transaction.userId }
            .mapKeys { getProfile(it.key) }
            .maxByOrNull { (_, transactions) -> transactions.sumBy { it.price } }
    }

    private fun getTotalSpendingToMostSpent(filterType: HomeFilterType): Int? {
        return getUserAndTransactionsToMostSpent(filterType)?.value?.sumBy { it.price }
    }

    private fun getProfile(userId: String): String? {
        return takeIf { id == userId }
            ?.let { profile }
            ?: group.find { it.id == userId }?.profile
    }

    private fun calculateUsageRateOfBudget(totalSpending: Int): Float {
        return if (totalSpending == 0) {
            0f
        } else {
            val rate = (totalSpending.toFloat() / user.budget.toFloat() * 100)
            rate.takeIf { it <= 100 } ?: 100f
        }
    }
}
