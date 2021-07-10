package dev.kxxcn.woozoora.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.navGraphViewModels
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.databinding.InputFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.edit.EditFilterType

class InputFragment : BaseFragment<InputFragmentBinding>() {

    override val viewModel by navGraphViewModels<InputViewModel>(R.id.nav_graph)

    private val filterType: EditFilterType?
        get() = arguments?.get(KEY_EDIT_ITEM) as? EditFilterType

    private val content: String?
        get() = arguments?.get(KEY_EDIT_CONTENT) as? String

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
        setupKeyboard()
        start()
    }

    private fun setupKeyboard() {
        showKeyboard(findViewByFilterType())
    }

    private fun findViewByFilterType(): EditText {
        return when (filterType) {
            EditFilterType.PRICE -> binding.priceEdit
            else -> binding.nameEdit
        }
    }

    private fun start() {
        viewModel.start(filterType, content)
    }
}
