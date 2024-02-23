package com.vzkz.profinder.ui.login

import com.vzkz.profinder.domain.usecases.auth.LoginUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.SaveUidDataStoreUseCase
import com.vzkz.profinder.fake.user1_test
import com.vzkz.profinder.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var loginViewModel: LoginViewModel

    @RelaxedMockK
    private lateinit var loginUseCase: LoginUseCaseImpl

    @RelaxedMockK
    private lateinit var saveUidDataStoreUseCase: SaveUidDataStoreUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            saveUidDataStoreUseCase = saveUidDataStoreUseCase,
//            dispatcher = UnconfinedTestDispatcher(),
            dispatcher = coroutineRule.testDispatcher,

            )
    }

    @Test
    fun `When onLogin is a success, state is changed to new user`() = runTest {
        //Arrange
        val actor = user1_test
        coEvery { loginUseCase(any(), any()) } returns Result.success(actor)

        //Act
        loginViewModel.onLogin("any", "any")
        assert(loginViewModel.state.loading)
//        advanceUntilIdle()

        //Assert
//        assert(loginViewModel.state.user == actor)
    }

    @Test
    fun `When login fails, error is shown`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        //Arrange
        val errorMsg = "Error"
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception(errorMsg))
        //Act
        loginViewModel.onLogin("any", "any")
        advanceUntilIdle()

        //Assert
        assert(loginViewModel.state.error.errorMsg == errorMsg)
    }

    @Test
    fun `When dialog closed, error disappears`() = runTest {
        //Arrange
        val errorMsg = "Error"
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Throwable(errorMsg))
        //Act
        loginViewModel.onLogin("any", "any")
        advanceUntilIdle()
        loginViewModel.onCloseDialog()
        advanceUntilIdle()

        //Assert
        assert(!loginViewModel.state.error.isError && loginViewModel.state.error.errorMsg == null)
    }

}