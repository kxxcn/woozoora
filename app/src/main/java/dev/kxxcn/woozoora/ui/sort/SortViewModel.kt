package dev.kxxcn.woozoora.ui.sort

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.InvalidFilterTypeException
import dev.kxxcn.woozoora.common.KEY_BRANCH_TYPE
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.*
import dev.kxxcn.woozoora.domain.model.AssetCategoryData
import dev.kxxcn.woozoora.domain.model.TransactionCategoryData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import dev.kxxcn.woozoora.ui.sort.item.SortItem
import kotlinx.coroutines.launch

class SortViewModel @AssistedInject constructor(
    observeAssetCategoryUseCase: ObserveAssetCategoryUseCase,
    observeTransactionCategoryUseCase: ObserveTransactionCategoryUseCase,
    private val deleteAssetCategoryUseCase: DeleteAssetCategoryUseCase,
    private val deleteTransactionCategory: DeleteTransactionCategoryUseCase,
    private val updateAssetCategoryUseCase: UpdateAssetCategoryUseCase,
    private val updateTransactionCategoryUseCase: UpdateTransactionCategoryUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<SortViewModel>

    private val currentFilterType = savedStateHandle
        .get<EditBranchType>(KEY_BRANCH_TYPE)
        ?: throw InvalidFilterTypeException()

    val editCategoryStringRes = if (currentFilterType == EditBranchType.TRANSACTION) {
        R.string.edit_category_of_transaction
    } else {
        R.string.edit_category_of_asset
    }

    private val forceObserve = MutableLiveData<Boolean>()

    val category = forceObserve.switchMap { isTransaction ->
        if (isTransaction) {
            observeTransactionCategoryUseCase().map { asSortItemFromTransaction(it) }
        } else {
            observeAssetCategoryUseCase().map { asSortItemFromAsset(it) }
        }
    }

    val deleteSet = MutableLiveData(mutableSetOf<String>())

    val deleteCount = deleteSet.map { it.size }

    private val _deleteEvent = MutableLiveData<Event<Int>>()
    val deleteEvent: LiveData<Event<Int>> = _deleteEvent

    private val _createEvent = MutableLiveData<Event<EditBranchType>>()
    val createEvent: LiveData<Event<EditBranchType>> = _createEvent

    init {
        forceObserve.value = currentFilterType == EditBranchType.TRANSACTION
    }

    fun onPress(id: String) {
        val set = deleteSet.value ?: throw RuntimeException()
        if (isPressed(id)) {
            set.remove(id)
        } else {
            set.add(id)
        }
        deleteSet.value = set
    }

    fun isPressed(id: String): Boolean {
        return deleteSet.value?.contains(id) ?: return false
    }

    fun confirmDeletion() {
        val set = deleteSet.value
        if (set == null) {
            clear()
        } else {
            val count = set.size
            _deleteEvent.value = Event(count)
        }
    }

    fun deleteAll() {
        val set = deleteSet.value ?: return
        viewModelScope.launch {
            when (currentFilterType) {
                EditBranchType.TRANSACTION -> deleteTransactionCategory(
                    set.map { it.toInt() }.toList()
                )
                EditBranchType.ASSET -> deleteAssetCategoryUseCase(
                    set.toList()
                )
            }.ifSucceeded {
                toast(R.string.complete_deletion_to_category)
                clear()
            }
        }
    }

    fun create() {
        _createEvent.value = Event(currentFilterType)
    }

    fun sort() {
        viewModelScope.launch {
            when (currentFilterType) {
                EditBranchType.TRANSACTION -> {
                    val sortList = category.value
                        ?.map {
                            TransactionCategoryData(
                                it.id.toInt(),
                                it.category,
                                it.priority
                            )
                        } ?: emptyList()
                    updateTransactionCategoryUseCase(sortList)
                }
                EditBranchType.ASSET -> {
                    val sortList = category.value
                        ?.map {
                            AssetCategoryData(
                                it.id,
                                it.category,
                                it.priority
                            )
                        } ?: emptyList()
                    updateAssetCategoryUseCase(sortList)
                }
            }
        }
    }

    private fun asSortItemFromTransaction(from: List<TransactionCategoryData>?): List<SortItem> {
        return from
            ?.map { SortItem(it.id.toString(), it.category, it.priority) }
            ?: emptyList()
    }

    private fun asSortItemFromAsset(from: List<AssetCategoryData>?): List<SortItem> {
        return from
            ?.map { SortItem(it.id, it.category, it.priority) }
            ?: emptyList()
    }

    private fun clear() {
        val set = deleteSet.value ?: return
        set.clear()
        deleteSet.value = set
    }
}
