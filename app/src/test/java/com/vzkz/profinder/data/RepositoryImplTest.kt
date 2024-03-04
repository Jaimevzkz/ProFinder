package com.vzkz.profinder.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.data.firebase.StorageService
import com.vzkz.profinder.user1_test
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryImplTest{
    /*
    * Functions tested:
    *   - login
    *   - getUserFromFirestore
    *   - signUp
    * */

    private lateinit var repositoryImpl: RepositoryImpl

    @RelaxedMockK
    private lateinit var authService: AuthService

    @RelaxedMockK
    private lateinit var firestoreService: FirestoreService

    @RelaxedMockK
    private lateinit var storageService: StorageService

    @RelaxedMockK
    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        repositoryImpl = RepositoryImpl(authService, firestoreService, storageService, context)
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

        coEvery { storageService.getProfilePhoto(any()) } returns null

        //Act
        val result = repositoryImpl.login("", "")

        //Assert
        assert(result.isSuccess)
        assert(result.getOrNull() == user1_test)
    }
    
    @Test
    fun `When firestoreService throws an exception, getUserFromFirestore throws an exception`() = runTest{
        val exceptionMsg = "Network failure while checking user existence"
        //Arrange
        every { context.getString(any()) } returns exceptionMsg
        coEvery { firestoreService.getUserData(any()) } throws Exception()

        //Act
        val result = runCatching { repositoryImpl.getUserFromFirestore("") }

        //Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == exceptionMsg)
    }

    @Test
    fun `When nickname does not exist and authService signUp is successful, signUp returns Result success`() = runTest {
        // Arrange
        val email = "test@test.com"
        val password = "password"

        val actorModel = user1_test

        every { firebaseUser.uid } returns user1_test.uid
        coEvery { firestoreService.nicknameExists(any()) } returns false
        coEvery { authService.signUp(email, password) } returns firebaseUser
        coEvery { firestoreService.insertUser(actorModel) } just Runs

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result.isSuccess)
        assertEquals(actorModel, result.getOrNull())
    }

    @Test
    fun `When nickname already exists, returns failure`() = runTest {
        // Arrange
        val email = "test@test.com"
        val password = "password"


        coEvery { firestoreService.nicknameExists(any()) } returns true
        every { context.getString(any()) } returns "Username already in use"

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Username already in use")
    }

    @Test
    fun `When authService signUp fails, returns failure`() = runTest {
        // Arrange
        val email = "test@test.com"
        val password = "password"
        val exceptionMsg = "An account with that email already exists. The account wasn\\'t created."

        coEvery { firestoreService.nicknameExists(any()) } returns false
        every { context.getString(any()) } returns exceptionMsg
        coEvery { authService.signUp(email, password) } throws Exception()

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == exceptionMsg)
    }

    @Test
    fun `When db insertion fails, returns failure`() = runTest {
        // Arrange
        val email = "test@test.com"
        val password = "password"
        val exceptionMsg = "Couldn\\'t insert user in database"

        coEvery { firestoreService.nicknameExists(any()) } returns false
        every { context.getString(any()) } returns exceptionMsg
        coEvery { authService.signUp(email, password) } returns firebaseUser
        coEvery { firestoreService.insertUser(any()) } throws Exception()

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == exceptionMsg)
    }

}