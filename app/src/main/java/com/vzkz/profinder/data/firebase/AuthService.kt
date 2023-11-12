package com.vzkz.profinder.data.firebase

import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.vzkz.profinder.R
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthService @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    suspend fun login(email: String, password: String): FirebaseUser? {

        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = it.user
                    cancellableContinuation.resume(user)
                }
                .addOnFailureListener {
                    cancellableContinuation.resumeWithException(it)
                }

        }
    }


    suspend fun signUp(email: String, password: String): FirebaseUser? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val profileUpdates = UserProfileChangeRequest
                        .Builder()
                        .setDisplayName("1234")
                        .build()

                    val user = it.user
                    user?.updateProfile(profileUpdates)?.addOnSuccessListener {
                        cancellableContinuation.resume(user)
                    }?.addOnFailureListener {
                        val exception = Exception(Resources.getSystem().getString(R.string.this_email_is_already_in_use))
                        cancellableContinuation.resumeWithException(exception)
                    }
                }
                .addOnFailureListener {
                    cancellableContinuation.resumeWithException(it)
                }
        }
    }

    fun isUserLogged(): Boolean = getCurrentUser() != null

    fun logout() = firebaseAuth.signOut()

    private fun getCurrentUser() = firebaseAuth.currentUser

}