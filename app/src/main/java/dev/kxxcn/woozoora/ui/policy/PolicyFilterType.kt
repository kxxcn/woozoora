package dev.kxxcn.woozoora.ui.policy

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.data.source.api.ApiService

enum class PolicyFilterType(
    val url: String,
    val nameRes: Int,
) {

    PERSONAL(ApiService.POLICY_PERSONAL_INFORMATION, R.string.terms),

    SOURCE(ApiService.POLICY_OPEN_SOURCE, R.string.open_source_license)
}
