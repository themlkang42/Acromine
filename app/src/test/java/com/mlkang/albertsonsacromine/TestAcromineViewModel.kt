package com.mlkang.albertsonsacromine

import android.util.Log
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class TestAcromineViewModel {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeLongForm = LongForm(
        "ABC",
        42,
        1991,
        emptyList()
    )

    private lateinit var mockAcromineRepository: AcromineRepository

    private lateinit var viewModel: AcromineViewModel
    private fun setup(isSuccess: Boolean = true) {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        mockAcromineRepository = mockk<AcromineRepository>()
        if (isSuccess) {
            coEvery { mockAcromineRepository.getLongForms(any()) } returns listOf(fakeLongForm)
        } else {
            coEvery { mockAcromineRepository.getLongForms(any()) } throws Throwable()
        }
        viewModel = AcromineViewModel(mockAcromineRepository)
    }

    @Test
    fun `test initial state`() = runTest {
        setup()

        assertEquals("", viewModel.acronymState.value)
        assertEquals(NetworkResult.Success(emptyList<LongForm>()), viewModel.longFormsState.value)
    }

    @Test
    fun `test updating acronym success`() = runTest {
        setup()

        viewModel.longFormsState.drop(1).test {
            val acronym = "ABC"
            viewModel.updateAcronymInput(acronym)
            mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(AcromineViewModel.DEBOUNCE_MILLIS + 1)

            assertEquals(acronym, viewModel.acronymState.value)
            assertEquals(NetworkResult.Loading, awaitItem())
            assertEquals(NetworkResult.Success(listOf(fakeLongForm)), awaitItem())
        }
    }

    @Test
    fun `test updating acronym fail`() = runTest {
        setup(false)

        viewModel.updateAcronymInput("ABC")
        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(AcromineViewModel.DEBOUNCE_MILLIS + 2)
        assertTrue(viewModel.longFormsState.value is NetworkResult.Failure)
    }

    @Test
    fun `test updating acronym debounce`() = runTest {
        setup()

        viewModel.updateAcronymInput("ABC")
        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(AcromineViewModel.DEBOUNCE_MILLIS - 1)
        viewModel.updateAcronymInput("ABCD")
        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(AcromineViewModel.DEBOUNCE_MILLIS + 1)

        coVerify(exactly = 1) { mockAcromineRepository.getLongForms(any()) }
    }
}