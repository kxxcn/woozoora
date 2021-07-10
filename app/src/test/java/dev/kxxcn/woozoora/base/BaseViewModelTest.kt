package dev.kxxcn.woozoora.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.kxxcn.woozoora.MainCoroutineRule
import dev.kxxcn.woozoora.data.source.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
@FlowPreview
open class BaseViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: FakeRepository

    @Before
    fun setupRepository() {
        repository = FakeRepository()
    }
}
