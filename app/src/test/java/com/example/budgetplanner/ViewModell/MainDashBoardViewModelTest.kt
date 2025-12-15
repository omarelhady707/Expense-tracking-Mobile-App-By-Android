package com.example.budgetplanner.ViewModell

import com.example.budgetplanner.MainDispatcherRule
import com.example.budgetplanner.ViewModell.MainDashBoardViewModel
import com.example.expensetrackingapp.Data.ExpenseDAO
import com.example.expensetrackingapp.Data.ExpenseEntity
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.UserDAO
import com.example.expensetrackingapp.Data.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class MainDashBoardViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var db: ExpenseUserDataBase
    private lateinit var userDao: UserDAO
    private lateinit var expenseDao: ExpenseDAO
    private lateinit var viewModel: MainDashBoardViewModel

    private val testUserId = 1

    // Dummy Data
    private val dummyUser = UserEntity(testUserId, "Test", "email", "pass", 0.0, 1000.0)
    private val dummyExpense = ExpenseEntity(
        userId = testUserId,
        amount = 100.0,
        date = "2024-01-01",
        category = "Food"
    )

    @Before
    fun setup() {
        db = mock()
        userDao = mock()
        expenseDao = mock()

        whenever(db.getUserDao()).thenReturn(userDao)
        whenever(db.getExpenseDao()).thenReturn(expenseDao)
    }

    @Test
    fun `getUserinfo initial state`() = runTest {
        //  delay the mock response to simulate "loading" state
        whenever(userDao.getUserById(testUserId)).thenAnswer {
            Thread.sleep(100) // Simulate network delay
            dummyUser
        }

        viewModel = MainDashBoardViewModel(db, testUserId)
        assertNull(viewModel.userinfo.value)
    }

    @Test
    fun `getUserexpanses initial state`() = runTest {
        viewModel = MainDashBoardViewModel(db, testUserId)
        // Before async work completes, list should be empty
        assertEquals(emptyList<ExpenseEntity>(), viewModel.userexpanses.value)
    }

    @Test
    fun `getBalancePrice initial state`() = runTest {
        viewModel = MainDashBoardViewModel(db, testUserId)
        assertEquals(0.0, viewModel.BalancePrice.value, 0.0)
    }

    @Test
    fun `load userinfo with invalid userId`() = runTest {
        whenever(userDao.getUserById(testUserId)).thenReturn(null)

        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        assertNull(viewModel.userinfo.value)
    }

    @Test
    fun `load userinfo database error`() = runTest {
        whenever(userDao.getUserById(any())).thenThrow(RuntimeException("DB Crash"))

        try {
            viewModel = MainDashBoardViewModel(db, testUserId)
            advanceUntilIdle()
        } catch (e: Exception) {
        }
    }

    @Test
    fun `load userExpances with no expenses`() = runTest {
        whenever(expenseDao.get_all_Expense_aboutUser(testUserId)).thenReturn(emptyList())

        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        assertEquals(emptyList<ExpenseEntity>(), viewModel.userexpanses.value)
    }

    @Test
    fun `load userExpances triggers CalcBalancePrice`() = runTest {
        // CalcBalancePrice calls getUserDao().updateTotExpense.
        whenever(userDao.getUserById(testUserId)).thenReturn(dummyUser)
        whenever(expenseDao.get_all_Expense_aboutUser(testUserId)).thenReturn(listOf(dummyExpense))

        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        // 100.0 is the amount in dummyExpense
        verify(userDao).updateTotExpense(testUserId, 100.0)
    }

    @Test
    fun `CalcBalancePrice with zero expenses`() = runTest {
        whenever(userDao.getUserById(testUserId)).thenReturn(dummyUser)
        whenever(expenseDao.get_all_Expense_aboutUser(testUserId)).thenReturn(emptyList())

        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        // Balance should equal budget (1000.0)
        assertEquals(1000.0, viewModel.BalancePrice.value, 0.01)
    }

    @Test
    fun `CalcBalancePrice with negative balance`() = runTest {
        val bigExpense = ExpenseEntity(
            userId = testUserId,
            amount = 1500.0,
            date = "date",
            category = "cat"
        )
        whenever(userDao.getUserById(testUserId)).thenReturn(dummyUser)
        whenever(expenseDao.get_all_Expense_aboutUser(testUserId)).thenReturn(listOf(bigExpense))

        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        // Expected: 1000 - 1500 = -500
        assertEquals(-500.0, viewModel.BalancePrice.value, 0.01)
    }

    @Test
    fun `CalcBalancePrice when userinfo is null`() = runTest {
        // User not found
        whenever(userDao.getUserById(testUserId)).thenReturn(null)
        whenever(expenseDao.get_all_Expense_aboutUser(testUserId)).thenReturn(listOf(dummyExpense))

        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        // Should not crash, balance remains 0
        assertEquals(0.0, viewModel.BalancePrice.value, 0.0)
        verify(userDao, never()).updateTotExpense(any(), any())
    }

    @Test
    fun `CalcBalancePrice checks database update`() = runTest {
        whenever(userDao.getUserById(testUserId)).thenReturn(dummyUser)
        whenever(expenseDao.get_all_Expense_aboutUser(testUserId)).thenReturn(listOf(dummyExpense))

        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        verify(userDao).updateTotExpense(testUserId, 100.0)
    }

    @Test
    fun `deleteExpenseById with invalid expenseId`() = runTest {
        viewModel = MainDashBoardViewModel(db, testUserId)

        viewModel.deleteExpenseById(-1)
        advanceUntilIdle()

        verify(expenseDao).deleteExpensePerId(-1)
    }

    @Test
    fun `deleteExpenseById triggers data refresh and recalculation`() = runTest {
        viewModel = MainDashBoardViewModel(db, testUserId)

        viewModel.deleteExpenseById(1)
        advanceUntilIdle()

        verify(expenseDao, times(2)).get_all_Expense_aboutUser(testUserId)
    }

    @Test
    fun `ViewModel Init block triggers data loads`() = runTest {
        viewModel = MainDashBoardViewModel(db, testUserId)
        advanceUntilIdle()

        verify(userDao).getUserById(testUserId)
        verify(expenseDao).get_all_Expense_aboutUser(testUserId)
    }

    @Test
    fun `Concurrent data modifications`() = runTest {
        viewModel = MainDashBoardViewModel(db, testUserId)

        // Call delete multiple times rapidly
        viewModel.deleteExpenseById(1)
        viewModel.deleteExpenseById(2)
        advanceUntilIdle()

        verify(expenseDao).deleteExpensePerId(1)
        verify(expenseDao).deleteExpensePerId(2)
    }
}
