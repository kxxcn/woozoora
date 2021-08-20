package dev.kxxcn.woozoora.ui.code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.woozoora.databinding.CodeFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class CodeFragment : BaseFragment<CodeFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<CodeViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = CodeFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.viewModel = this@CodeFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupKeyboard()
    }

    private fun setupKeyboard() {
        showKeyboard(binding.codeEdit)
    }
}
