package dev.kxxcn.woozoora.ui.intro

import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.user.model.User
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_INVITATION_ITEM
import dev.kxxcn.woozoora.common.extension.toData
import dev.kxxcn.woozoora.common.extension.year
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.succeeded
import dev.kxxcn.woozoora.data.userNotFound
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.SaveUserUseCase
import dev.kxxcn.woozoora.domain.UpdateTokenUseCase
import dev.kxxcn.woozoora.domain.model.AccountData
import dev.kxxcn.woozoora.domain.model.InvitationData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.util.CodeGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
class IntroViewModel @AssistedInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateTokenUseCase: UpdateTokenUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<IntroViewModel>

    val budgetEdit = MutableLiveData<String?>()

    val yearEdit = MutableLiveData<String?>()

    val editEvent = MediatorLiveData<Event<Pair<Int, Int>>>().apply {
        addSource(budgetEdit) { value = validateBudget(it) }
        addSource(yearEdit) { value = validateYear(it) }
    }

    private val _googleEvent = MutableLiveData<Event<Unit>>()
    val googleEvent: LiveData<Event<Unit>> = _googleEvent

    private val _kakaoEvent = MutableLiveData<Event<Unit>>()
    val kakaoEvent: LiveData<Event<Unit>> = _kakaoEvent

    private val _nextEvent = MutableLiveData<Event<Int>>()
    val nextEvent: LiveData<Event<Int>> = _nextEvent

    private val _previousEvent = MutableLiveData<Event<Int>>()
    val previousEvent: LiveData<Event<Int>> = _previousEvent

    private val _homeEvent = MutableLiveData<Event<Unit>>()
    val homeEvent: LiveData<Event<Unit>> = _homeEvent

    private var filterType: IntroFilterType? = null

    private var account: AccountData? = null

    private var saveJob: Job? = null

    fun select(endId: Int) {
        when (endId) {
            R.id.scene_kakao -> IntroFilterType.KAKAO
            R.id.scene_google -> IntroFilterType.GOOGLE
            else -> null
        }?.let {
            setFiltering(it)
        }
    }

    fun signIn() {
        when (filterType) {
            IntroFilterType.KAKAO -> signInForKakao()
            IntroFilterType.GOOGLE -> signInForGoogle()
            null -> toast(R.string.select_a_sign_in_method)
        }
    }

    fun validateUser(
        googleAccount: GoogleSignInAccount? = null,
        kakaoUser: User? = null,
    ) {
        account = googleAccount?.toData() ?: kakaoUser?.toData()

        viewModelScope.launch {
            val result = getUserUseCase(account?.id)
            when {
                result.succeeded -> updateTokenAndShowHomeScreen()
                result.userNotFound -> nextIfFilterTypeIsNotNull()
                result is Result.Error -> toast(result)
            }
        }
    }

    fun previous(currentState: Int) {
        if (currentState == R.id.scene_budget_prepare || currentState == R.id.scene_budget_complete) {
            val endId = getSceneIdByFilterType()
            _previousEvent.value = Event(endId)
        } else if (currentState == R.id.scene_year_prepare || currentState == R.id.scene_year_complete) {
            _previousEvent.value = Event(R.id.scene_budget_complete)
        } else {
            finish()
        }
    }

    fun next() {
        budgetEdit.value
            .takeIf { it != null && it.isNotEmpty() && it.toInt() > 0 }
            ?.let { enterYear() }
            ?: toast(R.string.enter_your_budget)
    }

    fun start() {
        yearEdit.value
            .takeIf { it?.length == 4 && it.toInt() <= Calendar.getInstance().year }
            ?.let {
                val budget = budgetEdit.value?.toLong() ?: 0
                register(budget, it.toInt())
            } ?: toast(R.string.check_the_year_of_birth)
    }

    private fun validateBudget(budget: String?): Event<Pair<Int, Int>> {
        return if (isValidBudget(budget)) {
            Event(R.id.scene_budget_prepare to R.id.scene_budget_complete)
        } else {
            Event(R.id.scene_budget_complete to R.id.scene_budget_prepare)
        }
    }

    private fun validateYear(year: String?): Event<Pair<Int, Int>> {
        return if (isValidYear(year)) {
            Event(R.id.scene_year_prepare to R.id.scene_year_complete)
        } else {
            Event(R.id.scene_year_complete to R.id.scene_year_prepare)
        }
    }

    private fun isValidBudget(budget: String?): Boolean {
        return try {
            budget != null && budget.toInt() > 0
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidYear(year: String?): Boolean {
        return try {
            year != null && year.length == 4
        } catch (e: Exception) {
            false
        }
    }

    private fun setFiltering(requestType: IntroFilterType) {
        filterType = requestType
    }

    private fun signInForKakao() {
        _kakaoEvent.value = Event(Unit)
    }

    private fun signInForGoogle() {
        _googleEvent.value = Event(Unit)
    }

    private fun updateTokenAndShowHomeScreen() {
        updateToken()
        showHomeScreen()
    }

    private fun updateToken() {
        viewModelScope.launch { updateTokenUseCase() }
    }

    private fun showHomeScreen() {
        _homeEvent.value = Event(Unit)
    }

    private fun nextIfFilterTypeIsNotNull() {
        val transitionId = getTransitionIdByFilterType()
        _nextEvent.value = Event(transitionId)
    }

    private fun getTransitionIdByFilterType(): Int {
        return when (filterType) {
            IntroFilterType.KAKAO -> R.id.transition_prepare_from_scene_kakao
            IntroFilterType.GOOGLE -> R.id.transition_prepare_from_scene_google
            else -> throw RuntimeException("Invalid intro filter type.")
        }
    }

    private fun getSceneIdByFilterType(): Int {
        return when (filterType) {
            IntroFilterType.KAKAO -> R.id.scene_kakao
            IntroFilterType.GOOGLE -> R.id.scene_google
            else -> throw RuntimeException("Invalid intro filter type.")
        }
    }

    private fun enterYear() {
        _nextEvent.value = Event(R.id.transition_year_from_budget)
    }

    private fun register(budget: Long, year: Int) {
        val id = account?.id
        if (id == null) {
            toast(R.string.user_information_cannot_be_verified)
        } else {
            if (saveJob?.isActive == true) return
            saveJob = viewModelScope.launch {
                val code = savedStateHandle.get<InvitationData>(KEY_INVITATION_ITEM)
                    ?.takeIf { it.hasInvitation }
                    ?.code
                    ?: CodeGenerator.random()

                val userData = UserData(
                    id,
                    code = code,
                    name = account?.name,
                    profile = account?.profile,
                    email = account?.email,
                    budget = budget,
                    createdDate = System.currentTimeMillis(),
                    year = year,
                    type = filterType?.ordinal ?: -2
                )
                val result = saveUserUseCase(userData)
                when {
                    result.succeeded -> showHomeScreen()
                    result is Result.Error -> toast(result)
                }
            }
        }
    }
}
