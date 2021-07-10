package dev.kxxcn.woozoora.ui.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.DaggerFragment
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.common.extension.displayWidth
import dev.kxxcn.woozoora.common.extension.drawable
import dev.kxxcn.woozoora.common.extension.hideKeyboard
import dev.kxxcn.woozoora.common.extension.showKeyboard
import dev.kxxcn.woozoora.domain.model.DatePickerData
import dev.kxxcn.woozoora.ui.custom.LoadingView
import dev.kxxcn.woozoora.ui.date.DatePickerFragment
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding> : DaggerFragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    lateinit var binding: T

    abstract val viewModel: BaseViewModel

    private val decorView: ViewGroup?
        get() = activity?.window?.decorView as? ViewGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListener()
    }

    override fun onDestroyView() {
        clearLoadingView()
        super.onDestroyView()
    }

    fun NavDirections.show() {
        try {
            findNavController().navigate(this)
        } catch (ignore: Exception) {
        }
    }

    fun close() {
        viewModel.close()
    }

    fun toast(any: Any?) {
        viewModel.toast(any)
    }

    fun showKeyboard(view: View) {
        activity?.showKeyboard(view)
    }

    fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun loading(isVisible: Boolean, fileName: String = "lottie_loading.json") {
        clearLoadingView()
        if (isVisible) {
            addLoadingView(fileName)
        }
    }

    fun <T> setSavedStateHandleToBackStackEntry(key: String, value: T) {
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(key, value)
    }

    fun <T> getSavedStateHandle(key: String, cleared: Boolean = true): T? {
        val savedStateHandle = parentFragment
            ?.findNavController()
            ?.currentBackStackEntry
            ?.savedStateHandle

        val value = savedStateHandle?.get<T>(key)

        if (cleared) savedStateHandle?.remove<T>(key)

        return value
    }

    fun openDialog(
        iconRes: Int,
        text: String,
        negative: (() -> Unit)? = null,
        positive: (() -> Unit)? = null,
    ): AlertDialog? {
        val context = context ?: return null
        val dialog = AlertDialog.Builder(context).create()
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.decision_selection_view, null).also {
            it.findViewById<ImageView>(R.id.decision_icon).setImageResource(iconRes)
            it.findViewById<TextView>(R.id.decision_text).text = text
            it.findViewById<TextView>(R.id.decision_no).setOnClickListener {
                dialog?.dismiss()
                negative?.invoke()
            }
            it.findViewById<TextView>(R.id.decision_yes).setOnClickListener {
                dialog?.dismiss()
                positive?.invoke()
            }
        }
        return dialog.also {
            it.setView(view)
            it.show()
            it.window?.setBackgroundDrawable(context.drawable(android.R.color.transparent))
            it.window?.setLayout((context.displayWidth * 0.7).toInt(), WRAP_CONTENT)
        }
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            onBackPressed(it)
        })
        viewModel.loadEvent.observe(viewLifecycleOwner, EventObserver {
            loading(it)
        })
        viewModel.hideEvent.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
        })
        viewModel.toastEvent.observe(viewLifecycleOwner, EventObserver {
            showToast(it)
        })
        viewModel.dateEvent.observe(viewLifecycleOwner, EventObserver {
            date(it)
        })
        viewModel.analyticsEvent.observe(viewLifecycleOwner, EventObserver {
            analytics(it)
        })

        viewModel.finishEvent.observe(viewLifecycleOwner, EventObserver {
            finish()
        })
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun onBackPressed(forceStop: Boolean) {
        if (forceStop) {
            requireActivity().finish()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun addLoadingView(fileName: String) {
        LoadingView(requireContext()).apply {
            isClickable = true
            isFocusable = true
            alpha = 0f
            lottieFile = fileName
            with(animate()) {
                startDelay = DURATION_QUARTER_SECONDS
                duration = DURATION_HALF_SECONDS
                alpha(1f)
                start()
            }
        }.also {
            decorView?.setTag(R.id.tag_loading_decor_view, it)
            decorView?.addView(it)
        }
    }

    private fun clearLoadingView() {
        val tag = decorView?.getTag(R.id.tag_loading_decor_view)
        val loadingView = tag as? View
        loadingView?.let {
            it.animate().cancel()
            decorView?.removeView(it)
        }
    }

    private fun showToast(message: Any) {
        if (message is Int) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else if (message is String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun date(datePicker: DatePickerData) {
        DatePickerFragment()
            .apply { arguments = bundleOf(KEY_PICKER_ITEM to datePicker) }
            .show(childFragmentManager, DatePickerFragment::class.java.name)
    }

    private fun analytics(nameRes: Int) {
        context?.let { firebaseAnalytics.logEvent(it.getString(nameRes), null) }
    }

    private fun finish() {
        requireActivity().finish()
    }
}

const val CURRENT_TRANSACTION_SAVED_STATE_KEY = "CURRENT_TRANSACTION_SAVED_STATE_KEY"
