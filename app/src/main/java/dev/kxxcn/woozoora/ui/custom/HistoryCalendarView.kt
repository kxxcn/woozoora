package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.woozoora.databinding.HistoryCalendarViewBinding
import dev.kxxcn.woozoora.ui.direction.history.item.CalendarItem

class HistoryCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private var binding: HistoryCalendarViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = HistoryCalendarViewBinding.inflate(inflater, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onDetachedFromWindow() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        super.onDetachedFromWindow()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun bind(item: CalendarItem) {
        with(binding) {
            this.lifecycleOwner = this@HistoryCalendarView
            this.item = item
            this.executePendingBindings()
        }
    }
}
