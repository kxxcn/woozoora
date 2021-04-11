package dev.kxxcn.woozoora.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.extension.doOnTransition
import dev.kxxcn.woozoora.databinding.SplashFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class SplashFragment : BaseFragment<SplashFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<SplashViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SplashFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.motionContainer.doOnTransition(onTransitionCompleted = { _, _ ->
            viewModel.start()
        })
        viewModel.introEvent.observe(viewLifecycleOwner, EventObserver {
            SplashFragmentDirections.actionSplashFragmentToIntroFragment(it).show()
        })
        viewModel.homeEvent.observe(viewLifecycleOwner, EventObserver {
            SplashFragmentDirections.actionSplashFragmentToDirectionFragment(it).show()
        })
    }
}
