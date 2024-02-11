package com.vzkz.profinder.ui.login

import android.util.Log
import com.vzkz.profinder.domain.usecases.LoginUseCaseImpl
import com.vzkz.profinder.fake.usecases.FakeLoginUseCase
import com.vzkz.profinder.fake.usecases.FakeSaveUidDataStoreUseCase
import com.vzkz.profinder.fake.user1_test
import com.vzkz.profinder.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest{

    @get:Rule
    val coroutineRule = CoroutineRule()

    @RelaxedMockK
    private lateinit var loginUseCase: LoginUseCaseImpl

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            saveUidDataStoreUseCase = FakeSaveUidDataStoreUseCase()
        )
    }

    @Test
    fun `Creating a viewModel exposes non loading state`(){
        val viewmodel = LoginViewModel(
            loginUseCase = FakeLoginUseCase(true),
            saveUidDataStoreUseCase = FakeSaveUidDataStoreUseCase()
        )

        assert(!viewmodel.state.loading)
    }

    @Test
    fun `When onLogin is a success, state is changed to new user`() = runTest{  //Wrong test, loading should assert true
        //Arrange
        val actor = user1_test
        coEvery { loginUseCase(any(), any()) } returns Result.success(actor)

        //Act
        loginViewModel.onLogin("any", "any")
        runCurrent()

        //Assert
        assert(loginViewModel.state.user == actor)
    }

}