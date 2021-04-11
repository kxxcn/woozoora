package dev.kxxcn.woozoora.ui.change

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.woozoora.databinding.ChangeFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class ChangeFragment : BaseFragment<ChangeFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<ChangeViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ChangeFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@ChangeFragment.viewModel
        }
        return binding.root
    }
}
