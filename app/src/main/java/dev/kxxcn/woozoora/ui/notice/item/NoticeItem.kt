package dev.kxxcn.woozoora.ui.notice.item

import dev.kxxcn.woozoora.domain.model.NoticeData

data class NoticeItem(
    val type: Int,
    val notice: NoticeData = NoticeData.empty(),
)
