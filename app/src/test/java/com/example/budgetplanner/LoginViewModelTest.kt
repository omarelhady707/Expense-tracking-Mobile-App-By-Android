//tests the logic of the LoginViewModel
package com.example.budgetplanner

import com.example.budgetplanner.ViewModell.LoginViewModel
import com.example.expensetrackingapp.Data.ExpenseUserDataBase
import com.example.expensetrackingapp.Data.UserDAO
import com.example.expensetrackingapp.Data.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var db: ExpenseUserDataBase
    private lateinit var userDao: UserDAO
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        db = mock()   // Creates fake versions of ExpenseUserDataBase and UserDAO
        userDao = mock()

        whenever(db.getUserDao()).thenReturn(userDao)

        viewModel = LoginViewModel(db)
    }


    @Test
    fun `login success when user exists`() = runTest {
        val user = UserEntity(
            userId = 1,
            name = "Test User",
            email = "test@test.com",
            Password = "1234",
            totExpense = 0.0,
            budget = 500.0
        )
        //The fake DB is told to return a valid UserEntity.
        whenever(userDao.login("test@test.com", "1234"))
            .thenReturn(user)

        viewModel.getUserByEmailandPassword("test@test.com", "1234")

        advanceUntilIdle()
        // Asserts that loginResult is true, the user_id is 1, and there are no errors.
        assertTrue(viewModel.loginResult.value)
        assertEquals(1, viewModel.user_id.value)
        assertEquals("", viewModel.loginError.value)
    }

    @Test
    fun `login fails when user not found`() = runTest {
        whenever(userDao.login("wrong@test.com", "0000"))
            .thenReturn(null) // The fake DB is told to return null.
        //The test calls the login function with wrong credentials.
        viewModel.getUserByEmailandPassword("wrong@test.com", "0000")

        advanceUntilIdle()
        // Asserts that loginResult is false and the error message is "Email or Password is incorrect".
        assertFalse(viewModel.loginResult.value)
        assertEquals("Email or Password is incorrect", viewModel.loginError.value)
    }
}
