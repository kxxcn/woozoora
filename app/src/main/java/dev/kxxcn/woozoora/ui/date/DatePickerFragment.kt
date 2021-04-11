package dev.kxxcn.woozoora.ui.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_QUARTER_SECONDS
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.base.Selectable
import dev.kxxcn.woozoora.databinding.DatePickerFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.DaggerBottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatePickerFragment : DaggerBottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    private lateinit var binding: DatePickerFragmentBinding

    private val viewModel by viewModels<DatePickerViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Woozoora_BottomSheetTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DatePickerFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@DatePickerFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
        setupBinding()
        setupListAdapter()
        setupListener()
    }

    private fun setupDialog() {
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).isDraggable = false
        }
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        with(binding.dateList) {
            isNestedScrollingEnabled = false
            adapter = DatePickerAdapter(viewModel)
        }
    }

    private fun setupListener() {
        viewModel.pickEvent.observe(viewLifecycleOwner, EventObserver {
            (parentFragment as? Selectable)?.selectDate(it)
            close()
        })
    }

    private fun close() {
        lifecycleScope.launch {
            delay(DURATION_QUARTER_SECONDS)
            dismissAllowingStateLoss()
        }
    }
}
