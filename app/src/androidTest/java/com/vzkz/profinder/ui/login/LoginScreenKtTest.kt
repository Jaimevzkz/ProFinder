package com.vzkz.profinder.ui.login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class LoginScreenKtTest{
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testLoginScreen(){
        composeTestRule.setContent {
            ScreenBody(
                onLogin =  { email, password -> /*TODO*/ },
                onSignUpClicked = { /*TODO*/ },
                onCloseDialog = { /*TODO*/ },
                error = UiError(false, null)
            )
        }

        composeTestRule.onNodeWithText("login", ignoreCase = true).assertExists()
    }



}