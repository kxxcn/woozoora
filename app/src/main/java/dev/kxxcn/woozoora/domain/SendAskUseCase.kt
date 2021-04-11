package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.AskData
import javax.inject.Inject


class SendAskUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(ask: AskData): Result<Any> {
        return repository.sendAsk(ask)
    }
}
