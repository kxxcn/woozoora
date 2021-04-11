package dev.kxxcn.woozoora.ui.policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.woozoora.databinding.PolicyFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class PolicyFragment : BaseFragment<PolicyFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<PolicyViewModel> {
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
        binding = PolicyFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@PolicyFragment.viewModel
        }
        return binding.root
    }
}
