package com.vzkz.profinder.ui.signup

import com.vzkz.profinder.domain.usecases.SignUpUseCase
import com.vzkz.profinder.fake.user1_test
import com.vzkz.profinder.util.CoroutineRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class SignUpViewModelTest{

    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val mockSignUpUseCase: SignUpUseCase = mock()


    @Test
    fun `Calling onCloseError puts error to false`(){
        //Arrange
        val valueToReturn = Result.success(user1_test)
        val expectedValue = user1_test
//        whenever(mockSignUpUseCase()).doReturn(liveDataToReturn)


    }






}