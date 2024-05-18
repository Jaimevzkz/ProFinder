package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.usecases.auth.LoginUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginUseCaseImplTest{

    @RelaxedMockK
    private lateinit var repository: Repository

    private lateinit var loginUseCase: LoginUseCaseImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginUseCase = LoginUseCaseImpl(repository)
    }

    @Test
    fun `when invoke loginUseCase with empty password result is error`() = runBlocking {
        //Arange
        val email = ""
        val password = ""

        coEvery {
            repository.login(any(), any())
        }.returns(Result.Error(FirebaseError.Authentication.UNKNOWN_ERROR))
        //Act
        loginUseCase(email, password)
        //Assert
        coVerify(exactly = 1) { repository.login(email, password) }
    }


}