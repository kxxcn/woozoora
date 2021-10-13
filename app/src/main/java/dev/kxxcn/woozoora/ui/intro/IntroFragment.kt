package dev.kxxcn.woozoora.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_HALF_SECONDS
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.REQUEST_CODE_SIGN_IN_GOOGLE
import dev.kxxcn.woozoora.common.extension.doOnTransition
import dev.kxxcn.woozoora.common.extension.isTransitionEnabled
import dev.kxxcn.woozoora.common.extension.toData
import dev.kxxcn.woozoora.databinding.IntroFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.util.KakaoSignInClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class IntroFragment : BaseFragment<IntroFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    @Inject
    lateinit var googleClient: GoogleSignInClient

    @Inject
    lateinit var kakaoClient: KakaoSignInClient

    override val viewModel by viewModels<IntroViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = IntroFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@IntroFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SIGN_IN_GOOGLE -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)
                    viewModel.validateUser(account?.toData())
                } catch (e: ApiException) {
                    toast(R.string.user_information_cannot_be_verified)
                }
            }
        }
    }

    override fun onDestroyView() {
        backPressedCallback.remove()
        super.onDestroyView()
    }

    private fun setupListener() {
        binding.motionContainer.doOnTransition(onTransitionStarted = { _, _, endId ->
            viewModel.select(endId)
        })
        viewModel.googleEvent.observe(viewLifecycleOwner, EventObserver {
            signInForGoogle()
        })
        viewModel.kakaoEvent.observe(viewLifecycleOwner, EventObserver {
            signInForKakao()
        })
        viewModel.nextEvent.observe(viewLifecycleOwner, EventObserver {
            nextAndFocus(it)
        })
        viewModel.previousEvent.observe(viewLifecycleOwner, EventObserver {
            previous(it)
        })
        viewModel.editEvent.observe(viewLifecycleOwner, EventObserver {
            transition(it)
        })
        viewModel.homeEvent.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
            showHomeScreen()
        })
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun transition(pair: Pair<Int, Int>) {
        val (beginId, endId) = pair
        with(binding.motionContainer) {
            if (isTransitionEnabled(beginId, endId)) {
                setTransition(beginId, endId)
                transitionToEnd()
            }
        }
    }

    private fun showHomeScreen() {
        IntroFragmentDirections.actionIntroFragmentToDirectionFragment().show()
    }

    private fun signInForGoogle() {
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, REQUEST_CODE_SIGN_IN_GOOGLE)
    }

    private fun signInForKakao() {
        lifecycleScope.launch {
            kakaoClient.signIn()
                .flatMapMerge { kakaoClient.userInfo() }
                .catch { toast(R.string.try_again_after_a_while) }
                .collect { viewModel.validateUser(it.toData()) }
        }
    }

    private fun nextAndFocus(transitionId: Int) {
        next(transitionId)
        focus(transitionId)
    }

    private fun next(transitionId: Int) {
        with(binding.motionContainer) {
            setTransition(transitionId)
            transitionToEnd()
        }
    }

    private fun focus(transitionId: Int) {
        lifecycleScope.launch {
            delay(DURATION_HALF_SECONDS)
            if (transitionId == R.id.transition_year_from_budget) {
                binding.yearEdit
            } else {
                binding.budgetEdit
            }.also {
                showKeyboard(it)
            }
        }
    }

    private fun previous(endId: Int) {
        with(binding.motionContainer) {
            setTransition(currentState, endId)
            transitionToEnd()
        }
    }

    private fun onBackPressed() {
        viewModel.previous(binding.motionContainer.currentState)
    }
}
