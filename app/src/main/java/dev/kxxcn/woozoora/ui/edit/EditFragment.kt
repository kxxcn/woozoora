package dev.kxxcn.woozoora.ui.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.common.FORMAT_TIME_HOUR_MINUTE_WITH_MARKER
import dev.kxxcn.woozoora.common.base.Inputable
import dev.kxxcn.woozoora.databinding.EditFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.MotionFragment
import dev.kxxcn.woozoora.ui.input.InputViewModel
import dev.kxxcn.woozoora.util.Converter
import java.util.*
import javax.inject.Inject

class EditFragment : MotionFragment<EditFragmentBinding>(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener,
    Inputable {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<EditViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    override val motionContainer: MotionLayout
        get() = binding.motionContainer

    override val endState: Int
        get() = R.id.scene_edit_end

    override val sharedViewModel by navGraphViewModels<InputViewModel>(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = EditFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@EditFragment.viewModel
            sharedViewModel = this@EditFragment.sharedViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
        start()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.onDateSet(year, month, dayOfMonth)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.onTimeSet(hourOfDay, minute)
    }

    private fun setupListAdapter() {
        with(binding.editList) {
            addItemDecoration(EditSpacingDecoration())
            adapter = EditAdapter(viewModel)
        }
    }

    private fun setupListener() {
        viewModel.editTransition.observe(viewLifecycleOwner, EventObserver {
            motionContainer.setTransition(it, endState)
        })
        viewModel.inputEvent.observe(viewLifecycleOwner, EventObserver {
            EditFragmentDirections.actionEditFragmentToInputFragment(it.first, it.second).show()
        })
        viewModel.datePickerEvent.observe(viewLifecycleOwner, EventObserver {
            showDatePicker(it)
        })
        viewModel.timePickerEvent.observe(viewLifecycleOwner, EventObserver {
            showTimePicker(it)
        })
        viewModel.receiptEvent.observe(viewLifecycleOwner, EventObserver {
            EditFragmentDirections.actionEditFragmentToReceiptFragment(it).show()
        })
        sharedViewModel.inputEvent.observe(viewLifecycleOwner, EventObserver {
            viewModel.setFilter(it)
        })
    }

    private fun start() {
        viewModel.start()
    }

    private fun showDatePicker(source: String?) {
        Converter.dateParse(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, source)?.let { date ->
            val c = Calendar.getInstance().apply { timeInMillis = date.time }
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            context?.let { DatePickerDialog(it, this@EditFragment, year, month, day).show() }
        }
    }

    private fun showTimePicker(source: String?) {
        Converter.dateParse(FORMAT_TIME_HOUR_MINUTE_WITH_MARKER, source)?.let { date ->
            val c = Calendar.getInstance().apply { timeInMillis = date.time }
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            context?.let { TimePickerDialog(it, this@EditFragment, hour, minute, false).show() }
        }
    }
}
