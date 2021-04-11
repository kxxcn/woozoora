package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.UserData
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(userId: String? = null): Result<UserData> {
        val dirtyCache = userId != null
        return repository.getUser(userId, dirtyCache)
    }
}
