package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(newToken: String) {
        repository.saveToken(newToken)
    }
}
