package com.vzkz.profinder.data.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.vzkz.profinder.domain.model.Constants.DESCRIPTION
import com.vzkz.profinder.domain.model.Constants.FIRSTNAME
import com.vzkz.profinder.domain.model.Constants.IS_USER
import com.vzkz.profinder.domain.model.Constants.LASTNAME
import com.vzkz.profinder.domain.model.Constants.NICKNAME
import com.vzkz.profinder.domain.model.Constants.PROFESSION
import com.vzkz.profinder.domain.model.Constants.STATE
import com.vzkz.profinder.domain.model.Constants.USERS_COLLECTION
import com.vzkz.profinder.fake.user2_test
import com.vzkz.profinder.fake.userDocument2_test
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class FirestoreServiceTest {

    @RelaxedMockK
    private lateinit var mockDB: FirebaseFirestore

    private lateinit var firestoreService: FirestoreService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        firestoreService = FirestoreService(mockDB)
    }

    // Mocks the simplest behaviour of a task so .await() can return task or throw exception
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
        val userMap = user2_test.toMap()

        every {
            mockDB.collection(USERS_COLLECTION).document(user2_test.uid)
                .set(userMap)
        } returns taskCompletionSource.task


        firestoreService.insertUser(user2_test)

        verify (exactly = 1) {
            mockDB
                .collection(USERS_COLLECTION)
                .document(user2_test.uid)
                .set(userMap, SetOptions.merge())
        }
    }

    @Test
    fun `When getUser succeds, ActorModel is returned`() = runTest {
        //Arrange
        val document = userDocument2_test

        val documentSnapshot = mockk<DocumentSnapshot> {
            every { data } returns document
        }

        every {
            mockDB.collection(USERS_COLLECTION).document(any())
                .get()
        } returns mockTask<DocumentSnapshot>(documentSnapshot)


        //Act
        val result = firestoreService.getUserData("any")

        //Assert
        assert(result.actor == user2_test.actor)
        assert(result.description == user2_test.description)
        assert(result.firstname == user2_test.firstname)
        assert(result.lastname == user2_test.lastname)
        assert(result.nickname == user2_test.nickname)
        assert(result.profession == user2_test.profession)
        assert(result.state == user2_test.state)
    }
    @Test
    fun `When getUser (when professional) doesnt find any of the fields in the map, exception thrown`() = runTest {
        //Arrange
        var document = userDocument2_test.toMutableMap()

        val documentSnapshot = mockk<DocumentSnapshot> {
            every { data } returns document
        }
        every {
            mockDB.collection(USERS_COLLECTION).document(any())
                .get()
        } returns mockTask<DocumentSnapshot>(documentSnapshot)

        for(field in userDocument2_test.keys){
            document.remove(field)
            //Assert
            assertThrows<Exception> { firestoreService.getUserData("any") }
            document = userDocument2_test.toMutableMap() //Restore the map
        }

    }
}

































