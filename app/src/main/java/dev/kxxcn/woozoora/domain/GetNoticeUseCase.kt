package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.NoticeData
import javax.inject.Inject

class GetNoticeUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(): Result<List<NoticeData>> {
        return repository.getNotice()
    }
}
