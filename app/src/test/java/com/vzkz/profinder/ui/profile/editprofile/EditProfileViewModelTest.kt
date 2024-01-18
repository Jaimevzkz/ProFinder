package com.vzkz.profinder.ui.profile.editprofile

import com.vzkz.profinder.fake.usecases.FakeGetUserUsecase
import com.vzkz.profinder.fake.usecases.FakeModifyUserDataUseCase
import com.vzkz.profinder.fake.user1
import com.vzkz.profinder.fake.user2
import com.vzkz.profinder.util.CoroutineRule
import org.junit.Rule
import org.junit.Test

class EditProfileViewModelTest {


    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun `first viewmodel test`() {
        //Arrange
        val viewmodel = EditProfileViewModel(
            modifyUserDataUseCase = FakeModifyUserDataUseCase(false),
            getUserUseCase = FakeGetUserUsecase()
        )

        //Act
        coroutineRule.testDispatcher.scheduler.runCurrent()
        viewmodel.onModifyUserData(user1, user2)

        //Assert
        assert(viewmodel.state.error.isError)

    }


}