package com.vzkz.profinder.data.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.vzkz.profinder.core.Constants.IS_ACTIVE
import com.vzkz.profinder.core.Constants.SERVICES_COLLECTION
import com.vzkz.profinder.core.Constants.USERS_COLLECTION
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.prof2_test
import com.vzkz.profinder.profDocument2_test
import com.vzkz.profinder.serviceDocument_test
import com.vzkz.profinder.userDocument1_test
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FirestoreServiceTest {

    @RelaxedMockK
    private lateinit var mockDB: FirebaseFirestore

    @MockK
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
    fun `Insert user succeds`() {
        //Arrange
        val taskCompletionSource =
            TaskCompletionSource<Void>() //used for testing calls that dont return data
        val userMap = prof2_test.toMap()

        every {
            mockDB.collection(USERS_COLLECTION).document(prof2_test.uid)
                .set(userMap)
        } returns taskCompletionSource.task


        firestoreService.insertUser(prof2_test)

        verify(exactly = 1) {
            mockDB
                .collection(USERS_COLLECTION)
                .document(prof2_test.uid)
                .set(userMap, SetOptions.merge())
        }
    }

    @Test
    fun `When getUser succeeds, ActorModel is returned`() = runTest {
        //Arrange
        val document = profDocument2_test

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
        assert(result is Result.Success)
        if(result is Result.Success){
            assert(result.data.actor == prof2_test.actor)
            assert(result.data.description == prof2_test.description)
            assert(result.data.firstname == prof2_test.firstname)
            assert(result.data.lastname == prof2_test.lastname)
            assert(result.data.nickname == prof2_test.nickname)
            assert(result.data.profession == prof2_test.profession)
            assert(result.data.state == prof2_test.state)

        }
    }

    @Test
    fun `When getUser (when professional) doesnt find any of the fields in the map, exception thrown`() =
        runTest {
            //Arrange
            var document = profDocument2_test.toMutableMap()

            val documentSnapshot = mockk<DocumentSnapshot> {
                every { data } returns document
            }
            every {
                mockDB.collection(USERS_COLLECTION).document(any())
                    .get()
            } returns mockTask<DocumentSnapshot>(documentSnapshot)

            for (field in profDocument2_test.keys) {
                document.remove(field)
                //Assert
                val result = firestoreService.getUserData("any")
                assert(result is Result.Error)
                document = profDocument2_test.toMutableMap() //Restore the map
            }
        }

    @Test
    fun `When getUser (when user) doesnt find any of the fields in the map, exception thrown`() =
        runTest {
            //Arrange
            var document = userDocument1_test.toMutableMap()

            val documentSnapshot = mockk<DocumentSnapshot> {
                every { data } returns document
            }
            every {
                mockDB.collection(USERS_COLLECTION).document(any())
                    .get()
            } returns mockTask<DocumentSnapshot>(documentSnapshot)

            for (field in userDocument1_test.keys) {
                document.remove(field)
                //Assert
                val result = firestoreService.getUserData("any")
                assert(result is Result.Error)
                document = profDocument2_test.toMutableMap() //Restore the map
            }
        }


    @Test
    fun `When any service field is not found, exception thrown`() = runTest {
        //Arrange
        //getService
        var document = serviceDocument_test.toMutableMap()
        val documentSnapshot = mockk<DocumentSnapshot> {
            every { data } returns document
        }
        val querySnapshot = mockk<QuerySnapshot> {
            every { documents } returns listOf(documentSnapshot)
        }
        every {
            mockDB.collection(SERVICES_COLLECTION).whereEqualTo(IS_ACTIVE, true).get()
        } returns mockTask<QuerySnapshot>(querySnapshot)

        //getUser
        val document2 = profDocument2_test
        val document2Snapshot = mockk<DocumentSnapshot> {
            every { data } returns document2
        }
        every {
            mockDB.collection(USERS_COLLECTION).document(any())
                .get()
        } returns mockTask<DocumentSnapshot>(document2Snapshot)


        for (field in serviceDocument_test.keys) {
            //Act
            document.remove(field)
            //Assert
            val result = firestoreService.getActiveServiceList()
            assert(result is Result.Error)

            document = profDocument2_test.toMutableMap() //Restore the map
        }
    }


}

































