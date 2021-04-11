package dev.kxxcn.woozoora.ui.notice.holder

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_QUARTER_SECONDS
import dev.kxxcn.woozoora.common.extension.displayWidth
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.databinding.NoticeContentItemBinding
import dev.kxxcn.woozoora.domain.model.NoticeData
import dev.kxxcn.woozoora.ui.notice.NoticeAdapter
import dev.kxxcn.woozoora.ui.notice.NoticeViewModel


class NoticeContentHolder(
    private val binding: NoticeContentItemBinding,
) : NoticeBaseHolder(binding) {

    private val expandAnimator: ValueAnimator?
        get() = binding.contentParent.getTag(R.id.tag_notice_expand_animator) as? ValueAnimator

    private val rotateAnimator: ValueAnimator?
        get() = binding.arrowIcon.getTag(R.id.tag_notice_rotate_animator) as? ValueAnimator

    private var isExpanded: Boolean
        get() = itemView.getTag(R.id.tag_notice_expand) as? Boolean ?: false
        set(value) = itemView.setTag(R.id.tag_notice_expand, value)

    private val width = context.displayWidth - 60.dpToPx
    private val widthMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST)
    private val heightMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

    init {
        itemView.setOnClickListener {
            cancelAnimation()
            startAnimation()
            saveCurrentState()
            scrollToPosition()
        }
    }

    override fun onDetach() {
        expandAnimator?.removeAllUpdateListeners()
        rotateAnimator?.removeAllUpdateListeners()
        super.onDetach()
    }

    override fun bind(viewModel: NoticeViewModel, notice: NoticeData, isExpand: Boolean) {
        setupBinding(viewModel, notice)
        setupLayout(isExpand)
    }

    private fun setupBinding(viewModel: NoticeViewModel, notice: NoticeData) {
        with(binding) {
            this.lifecycleOwner = this@NoticeContentHolder
            this.viewModel = viewModel
            this.notice = notice
            this.executePendingBindings()
        }
    }

    private fun setupLayout(isExpand: Boolean) {
        if (isExpand) {
            setContentParentHeight(measureChildView())
            setArrowIconRotation(180f)
        } else {
            setContentParentHeight(0)
            setArrowIconRotation(0f)
        }
        isExpanded = isExpand
    }

    private fun cancelAnimation() {
        expandAnimator?.cancel()
        rotateAnimator?.cancel()
    }

    private fun startAnimation() {
        val contentParent = binding.contentParent
        val arrowIcon = binding.arrowIcon

        if (isExpanded) {
            ValueAnimator.ofInt(
                contentParent.height,
                0
            ) to ValueAnimator.ofFloat(
                arrowIcon.rotation,
                0f
            )
        } else {
            ValueAnimator.ofInt(
                contentParent.height,
                measureChildView()
            ) to ValueAnimator.ofFloat(
                arrowIcon.rotation,
                180f
            )
        }.also {
            it.first.let { animator ->
                animator.duration = DURATION_QUARTER_SECONDS
                animator.addUpdateListener { anim -> setContentParentHeight(anim.animatedValue as Int) }
                animator.start()
                contentParent.setTag(R.id.tag_notice_expand_animator, it)
            }
            it.second?.let { animator ->
                animator.duration = DURATION_QUARTER_SECONDS
                animator.addUpdateListener { anim -> setArrowIconRotation(anim.animatedValue as Float) }
                animator.start()
                arrowIcon.setTag(R.id.tag_notice_rotate_animator, it)
            }
        }

        isExpanded = !isExpanded
    }

    private fun saveCurrentState() {
        val parent = itemView.parent as RecyclerView
        val adapter = parent.adapter as NoticeAdapter
        adapter.save(adapterPosition, isExpanded)
    }

    private fun scrollToPosition() {
        if (isExpanded) {
            val viewModel = binding.viewModel ?: return
            viewModel.scroll(adapterPosition)
        }
    }

    private fun measureChildView(): Int {
        binding.contentText.measure(widthMeasureSpec, heightMeasureSpec)
        return binding.contentText.measuredHeight + binding.contentParent.paddingTop + binding.contentParent.paddingBottom
    }

    private fun setContentParentHeight(height: Int) {
        binding.contentParent.updateLayoutParams { this.height = height }
    }

    private fun setArrowIconRotation(rotation: Float) {
        binding.arrowIcon.rotation = rotation
    }

    companion object {

        fun from(parent: ViewGroup): NoticeContentHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NoticeContentItemBinding.inflate(inflater, parent, false)
            return NoticeContentHolder(binding)
        }
    }
}
