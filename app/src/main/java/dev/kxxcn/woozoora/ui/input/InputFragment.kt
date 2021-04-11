package dev.kxxcn.woozoora.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.databinding.InputFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.edit.EditFilterType

class InputFragment : BaseFragment<InputFragmentBinding>() {

    override val viewModel by navGraphViewModels<InputViewModel>(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = InputFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@InputFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    private fun start() {
        val filterType = arguments?.get(KEY_EDIT_ITEM) as EditFilterType
        viewModel.start(filterType)
    }
}
