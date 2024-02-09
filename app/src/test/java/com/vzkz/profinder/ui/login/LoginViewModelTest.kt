package com.vzkz.profinder.ui.login

import com.vzkz.profinder.fake.usecases.FakeLoginUseCase
import com.vzkz.profinder.fake.usecases.FakeSaveUidDataStoreUseCase
import com.vzkz.profinder.util.CoroutineRule
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest{

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun `Creating a viewModel exposes loading state`(){
        val viewmodel = LoginViewModel(
            loginUseCase = FakeLoginUseCase(true),
            saveUidDataStoreUseCase = FakeSaveUidDataStoreUseCase()
        )

        assert(!viewmodel.state.loading)
    }

    @Test
    fun `When reducing a LoginIntent Loading, loading == true`(){ //Wrong test, loading should assert true
        //Arrange
        val viewmodel = LoginViewModel(
            loginUseCase = FakeLoginUseCase(true),
            saveUidDataStoreUseCase = FakeSaveUidDataStoreUseCase()
        )

        //Act
        coroutineRule.testDispatcher.scheduler.runCurrent()
        viewmodel.dispatch(LoginIntent.Loading(true))

        //Assert
        assert(!viewmodel.state.loading)
    }

}