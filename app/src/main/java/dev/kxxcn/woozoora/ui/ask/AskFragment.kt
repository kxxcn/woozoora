package dev.kxxcn.woozoora.ui.ask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.woozoora.databinding.AskFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class AskFragment : BaseFragment<AskFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<AskViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = AskFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@AskFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    private fun start() {
        viewModel.start()
    }
}
