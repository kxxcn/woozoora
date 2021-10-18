package dev.kxxcn.woozoora.ui.sort

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import dev.kxxcn.woozoora.common.InvalidHolderItemException
import dev.kxxcn.woozoora.common.base.OnStartDragListener
import dev.kxxcn.woozoora.databinding.SortItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.sort.item.SortItem

@SuppressLint("ClickableViewAccessibility")
class SortHolder(
    private val binding: SortItemBinding,
    private val dragListener: OnStartDragListener,
) : BaseHolder(binding) {

    init {
        binding.sortIcon.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                dragListener.onStartDrag(this@SortHolder)
            }
            true
        }
    }

    override fun onAttach() {
        super.onAttach()
        val viewModel = binding.viewModel ?: return
        with(binding.sortCheck) {
            isChecked = viewModel.isPressed(getSortItemId())
            binding.sortCheck.setOnCheckedChangeListener { _, b -> viewModel.onPress(getSortItemId()) }
        }
    }

    override fun onDetach() {
        binding.sortCheck.setOnCheckedChangeListener(null)
        super.onDetach()
    }

    fun bind(item: SortItem, viewModel: SortViewModel) {
        setupArguments(item, viewModel)
    }

    private fun setupArguments(item: SortItem, viewModel: SortViewModel) {
        with(binding) {
            this.lifecycleOwner = this@SortHolder
            this.item = item
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    private fun getSortItemId(): String {
        return binding.item?.id ?: throw InvalidHolderItemException()
    }

    companion object {

        fun from(parent: ViewGroup, dragListener: OnStartDragListener): SortHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SortItemBinding.inflate(inflater, parent, false)
            return SortHolder(binding, dragListener)
        }
    }
}
