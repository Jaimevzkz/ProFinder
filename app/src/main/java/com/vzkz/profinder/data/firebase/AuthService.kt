package com.vzkz.profinder.data.firebase

import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.R
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthService @Inject constructor(private val firebaseAuth: FirebaseAuth) { //TO Test

    suspend fun login(email: String, password: String): FirebaseUser? {//TO Test

        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    cancellableContinuation.resume(it.user)
                }
                .addOnFailureListener {
//                    val exception = FirebaseAuthException("", Resources.getSystem().getString(R.string.wrong_email_or_password))
                    cancellableContinuation.resumeWithException(it)
                }

        }
    }


    suspend fun signUp(email: String, password: String): FirebaseUser? {//TO Test
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    cancellableContinuation.resume(it.user)
                }
                .addOnFailureListener {
//                    val exception = Throwable(Resources.getSystem().getString(R.string.account_already_exists))
                    cancellableContinuation.resumeWithException(it)
                }
        }
    }

    fun isUserLogged(): Boolean = getCurrentUser() != null

    fun logout() = firebaseAuth.signOut()

    private fun getCurrentUser() = firebaseAuth.currentUser

}