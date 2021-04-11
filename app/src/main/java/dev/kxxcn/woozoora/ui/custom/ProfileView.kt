package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.woozoora.common.base.Animatable
import dev.kxxcn.woozoora.databinding.ProfileViewBinding
import dev.kxxcn.woozoora.domain.model.UserData

class ProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner, Animatable {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private val binding: ProfileViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ProfileViewBinding.inflate(inflater, this, true)
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

    override fun playAnimation() {
        val progress = binding.progress ?: 0
        binding.profileProgress.setProgress(progress, true)
    }

    fun bind(user: UserData, progress: Int, color: Int) {
        with(binding) {
            this.lifecycleOwner = this@ProfileView
            this.user = user
            this.progress = progress
            this.color = color
            this.executePendingBindings()
        }
    }
}
