package dev.kxxcn.woozoora.data.source.entity

data class OverviewEntity(
    val user: UserEntity?,
    val group: List<UserEntity>,
    val transactions: List<TransactionEntity>
)
