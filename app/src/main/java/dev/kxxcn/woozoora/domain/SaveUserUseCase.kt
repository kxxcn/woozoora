package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.UserData
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(user: UserData): Result<Any> {
        return repository.saveUser(user)
    }
}
