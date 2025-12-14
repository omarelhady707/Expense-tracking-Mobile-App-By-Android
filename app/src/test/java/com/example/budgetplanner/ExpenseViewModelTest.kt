package com.example.budgetplanner

import com.example.expensetrackingapp.Data.ExpenseDAO
import com.example.expensetrackingapp.Data.ExpenseEntity
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var db: ExpenseUserDataBase
    private lateinit var expenseDao: ExpenseDAO
    private lateinit var viewModel: ExpenseViewModel

    @Before
    fun setup() {
        db = mock()
        expenseDao = mock()

        whenever(db.getExpenseDao()).thenReturn(expenseDao)

        viewModel = ExpenseViewModel(db)
    }

    @Test
    fun `add expense calls dao`() = runTest {
        val expense = ExpenseEntity(
            userId = 1,
            amount = 100.0,
            date = "2024-01-01",
            category = "Food"
        )

        viewModel.AddExpense(expense)

        advanceUntilIdle()

        verify(expenseDao).addExpense(expense)
    }
}
