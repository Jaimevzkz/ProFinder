package com.vzkz.profinder.ui.signup

import com.vzkz.profinder.fake.user1_test
import com.vzkz.profinder.util.CoroutineRule
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()



    @Test
    fun `Calling onCloseError puts error to false`() {
        //Arrange
        val valueToReturn = Result.success(user1_test)
        val expectedValue = user1_test
//        whenever(mockSignUpUseCase()).doReturn(liveDataToReturn)


    }


}