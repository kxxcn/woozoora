package dev.kxxcn.woozoora.ui.base

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.common.extension.doOnTransition

abstract class MotionFragment<T : ViewDataBinding> : BaseFragment<T>() {

    abstract val motionContainer: MotionLayout

    abstract val endState: Int

    abstract override val viewModel: MotionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupMotionLayout()
    }

    private fun setupListener() {
        motionContainer.doOnTransition(onTransitionCompleted = { _, id ->
            viewModel.saveInstanceState(id)
        })
    }

    private fun setupMotionLayout() {
        viewModel.currentMotionState?.let { transitionIfStateIsFinished(it) }
    }

    private fun transitionIfStateIsFinished(state: Int) {
        if (state == endState) {
            motionContainer.transitionToEnd()
        }
    }
}
