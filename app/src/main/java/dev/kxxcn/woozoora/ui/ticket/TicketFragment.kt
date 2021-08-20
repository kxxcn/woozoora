package dev.kxxcn.woozoora.ui.ticket

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import dagger.android.support.DaggerDialogFragment
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.MATCH_PARENT
import dev.kxxcn.woozoora.common.WRAP_CONTENT
import dev.kxxcn.woozoora.common.extension.displayWidth
import dev.kxxcn.woozoora.databinding.TicketFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.direction.DirectionViewModel
import javax.inject.Inject

class TicketFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    private lateinit var binding: TicketFragmentBinding

    private val viewModel by viewModels<TicketViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private val sharedViewModel by viewModels<DirectionViewModel>(this::requireParentFragment)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            isCancelable = false
            dialog.window?.let {
                it.requestFeature(Window.FEATURE_NO_TITLE)
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                it.setWindowAnimations(R.style.BaseDialogFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setLayout(MATCH_PARENT, WRAP_CONTENT)
            it.attributes = it.attributes.apply {
                width = (requireContext().displayWidth * 0.9).toInt()
                gravity = Gravity.CENTER
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = TicketFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@TicketFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupListener()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            close()
        })
        viewModel.completeEvent.observe(viewLifecycleOwner, EventObserver {
            refreshAndClose()
        })
    }

    private fun refreshAndClose() {
        sharedViewModel.refresh()
        close()
    }

    private fun close() {
        dismissAllowingStateLoss()
    }
}
