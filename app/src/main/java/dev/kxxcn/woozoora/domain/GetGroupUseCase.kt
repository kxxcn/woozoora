package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.UserData
import javax.inject.Inject

class GetGroupUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(exclude: Boolean = true): Result<List<UserData>> {
        return repository.getGroup(exclude)
    }
}
