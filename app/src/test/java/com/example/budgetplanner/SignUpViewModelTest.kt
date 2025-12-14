//tests the logic of the SignUpViewModel
package com.example.budgetplanner

import com.example.budgetplanner.ViewModell.SignUpViewModel
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.UserDAO
import com.example.expensetrackingapp.Data.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var db: ExpenseUserDataBase
    private lateinit var userDao: UserDAO
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setup() {
        db = mock()
        userDao = mock()

        whenever(db.getUserDao()).thenReturn(userDao)

        viewModel = SignUpViewModel(db)
    }

    @Test
    //create dummy user object
    fun `add user returns generated id`() = runTest {
        val user = UserEntity(
            userId = 0,
            name = "Test User",
            email = "test@test.com",
            Password = "1234",
            totExpense = 0.0,
            budget = 0.0
        )

        whenever(userDao.addUser(user)).thenReturn(7L)

        viewModel.AddNewUserandGetId(user)

        advanceUntilIdle()
        // asserts that the ViewModel updated its user_id state to 7.
        // This confirms the ViewModel successfully communicated with the data layer and captured the result
        assertEquals(7, viewModel.user_id.value)
    }
}
