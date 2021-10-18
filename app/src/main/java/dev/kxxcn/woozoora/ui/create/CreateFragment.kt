package dev.kxxcn.woozoora.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.woozoora.databinding.CreateFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class CreateFragment : BaseFragment<CreateFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<CreateViewModel> {
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
        binding = CreateFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@CreateFragment.viewModel
        }
        return binding.root
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }
}
