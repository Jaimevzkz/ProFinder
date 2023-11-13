package com.vzkz.profinder.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.vzkz.profinder.MotherObject
import com.vzkz.profinder.MotherObject.defaultEmail
import com.vzkz.profinder.MotherObject.defaultPassword
import io.kotlintest.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.assertThrows
import org.junit.Test


class AuthServiceTest {


    @Test
    fun `When login fails exception should be thrown`() {

//        val authService = AuthService(FirebaseAuth.getInstance())
        val invalidEmail = "usuario@dominio.com"
        val invalidPassword = "contrasenaIncorrecta"

        // Verificar que se lance la excepción FirebaseAuthException

        val exception = assertThrows(FirebaseAuthException::class.java) {
            CoroutineScope(Dispatchers.IO).launch {
//                authService.login(invalidEmail, invalidPassword)
            }
        }

        exception.errorCode shouldBe "ERROR_INVALID_CREDENTIALS"
        exception.message shouldBe "Invalid credentials"


//        //Given
//        val defEmail = defaultEmail
//        val defPassword = defaultPassword
//        var exc: Exception?
//
//        //When
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val login = AuthService(MotherObject.mokkAuthService).login(defEmail, defPassword)
//            } catch (e: Exception){
//                exc = e
//                Log.e("JaimeTest", "${e.message} ... ${e.stackTrace}")
//            }
//
//        }
//
//        //Then

    }

//    fun signUp() {
//    }
//
//    fun isUserLogged() {
//    }
//
//    fun logout() {
//    }
}