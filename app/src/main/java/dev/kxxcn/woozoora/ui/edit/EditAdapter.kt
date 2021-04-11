package dev.kxxcn.woozoora.ui.edit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.base.IncomparableDiffCallback
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.edit.holder.*

class EditAdapter(
    private val viewModel: EditViewModel
) : BaseAdapter<Int, RecyclerView.ViewHolder>(IncomparableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NAME -> EditNameHolder.from(parent)
            TYPE_DATE -> EditDateHolder.from(parent)
            TYPE_PRICE -> EditPriceHolder.from(parent)
            TYPE_PAYMENT -> EditPaymentHolder.from(parent)
            TYPE_CATEGORY -> EditCategoryHolder.from(parent)
            TYPE_DESC -> EditDescriptionHolder.from(parent)
            else -> EditCompleteHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? EditBaseHolder)?.bind(viewModel)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object {

        const val TYPE_NAME = 0
        const val TYPE_DATE = 1
        const val TYPE_PRICE = 2
        const val TYPE_PAYMENT = 3
        const val TYPE_CATEGORY = 4
        const val TYPE_DESC = 5
        const val TYPE_COMPLETE = 6
    }
}
