package com.vzkz.profinder.data.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Executors
import com.google.firebase.firestore.util.Util.voidErrorTransformer
import com.vzkz.profinder.domain.model.Constants.USERS_COLLECTION
import com.vzkz.profinder.fake.user2_test
import com.vzkz.profinder.ui.login.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FirestoreServiceTest {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    //Mocks the simplest behaviour of a task so .await() can return task or throw exception
    inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
        val task: Task<T> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedT: T = mockk(relaxed = true)
        every { task.result } returns result
        return task
    }

    @Test
    fun `Insert user test`() {
        //Arrange
        val taskCompletionSource =
            TaskCompletionSource<Void>() //used for testing calls that dont return data
        val userToCreate = user2_test
        val userMap = user2_test.toMap()
        val mockDB: FirebaseFirestore = mockk {
            every {
                collection(USERS_COLLECTION).document(user2_test.uid)
                    .set(userMap)
            } returns taskCompletionSource.task.continueWith(
                Executors.DIRECT_EXECUTOR,
                voidErrorTransformer()
            )
        }

        val firestoreService = spyk(FirestoreService(mockDB), recordPrivateCalls = true)
        runBlocking {
            firestoreService.insertUser(userToCreate)

            coVerify(exactly = 1) { firestoreService.insertUser(userToCreate) }
            confirmVerified(firestoreService)

            verify(exactly = 1) {
                mockDB
                    .collection(USERS_COLLECTION)
                    .document(user2_test.uid)
                    .set(userMap)
            }
            confirmVerified(mockDB)
        }
    }
}

































