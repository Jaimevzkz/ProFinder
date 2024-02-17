package com.vzkz.profinder.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.fake.user1_test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RepositoryImplTest{
    /*
    * Methods tested:
    *   - login
    *   - getUserFromFirestore
    * */

    private lateinit var repositoryImpl: RepositoryImpl

    @RelaxedMockK
    private lateinit var authService: AuthService

    @RelaxedMockK
    private lateinit var firestoreService: FirestoreService

    @RelaxedMockK
    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        repositoryImpl = RepositoryImpl(authService, firestoreService, context)
    }

    @Test
    fun `When auth fails, login throws an Exception`() = runTest {
        //Arrange
        every { context.getString(any()) } returns "Wrong email or password. "
        coEvery { authService.login(any(), any()) } throws Exception()

        //Act
        val result = repositoryImpl.login("", "")

        //Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Wrong email or password. ")
    }

    @Test
    fun `When auth service returns null FirebaseUser, Exception(Error logging in user) is thrown`() = runTest{
        //Arrange
        every { context.getString(any()) } returns "Error logging in user"
        coEvery { authService.login(any(), any()) } returns null

        //Act
        val result = repositoryImpl.login("", "")

        //Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Error logging in user")
    }

    @Test
    fun `When authservice works, userModel is returned`() = runTest{
        //Arrange
        coEvery { authService.login(any(), any()) } returns firebaseUser

        coEvery { firestoreService.getUserData(any()) } returns user1_test

        //Act
        val result = repositoryImpl.login("", "")

        //Assert
        assert(result.isSuccess)
        assert(result.getOrNull() == user1_test)
    }

}