package com.vzkz.profinder.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.core.UidCombiner
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.data.firebase.RealtimeService
import com.vzkz.profinder.data.firebase.StorageService
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.user1_test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RepositoryImplTest{
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

    @RelaxedMockK
    private lateinit var realtimeService: RealtimeService

    @RelaxedMockK
    private lateinit var locationService: LocationService

    @RelaxedMockK
    private lateinit var uidCombiner: UidCombiner

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        repositoryImpl = RepositoryImpl(authService, firestoreService, storageService, realtimeService, uidCombiner, locationService)
    }

    @Test
    fun `When auth fails, login throws an Exception, and is parsed as error`() = runTest {
        //Arrange
        every { context.getString(any()) } returns "Wrong email or password. "
        coEvery { authService.login(any(), any()) } throws Exception()

        //Act
        val result = repositoryImpl.login("", "")


        //Assert
        assert(result is Result.Error && result.error == FirebaseError.Authentication.WRONG_EMAIL_OR_PASSWORD)
    }

    @Test
    fun `When auth service returns null FirebaseUser user not found error`() = runTest{
        //Arrange
        every { context.getString(any()) } returns "Error logging in user"
        coEvery { authService.login(any(), any()) } returns null

        //Act
        val result = repositoryImpl.login("", "")

        //Assert
        assert(result is Result.Error && result.error == FirebaseError.Firestore.USER_NOT_FOUND_IN_DATABASE)

    }

    @Test
    fun `When authservice works, userModel is returned`() = runTest{
        //Arrange
        coEvery { authService.login(any(), any()) } returns firebaseUser

        coEvery { firestoreService.getUserData(any()) } returns Result.Success(user1_test)

        coEvery { storageService.getProfilePhoto(any()) } returns null

        //Act
        val result = repositoryImpl.login("", "")

        //Assert
        assert(result is Result.Success && result.data == user1_test)
    }
    
    @Test
    fun `When firestoreService throws an exception, getUserFromFirestore returns error`() = runTest{
        val exceptionMsg = "Network failure while checking user existence"
        //Arrange
        every { context.getString(any()) } returns exceptionMsg
        coEvery { firestoreService.getUserData(any()) } returns Result.Error(FirebaseError.Firestore.CONNECTION_ERROR)

        //Act
        val result = repositoryImpl.getUserFromFirestore("")

        //Assert
        assert(result is Result.Error)
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
        coEvery { firestoreService.insertUser(actorModel) } returns Result.Success(Unit)

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result is Result.Success && result.data == actorModel)
    }

    @Test
    fun `When nickname already exists, returns failure`() = runTest {
        // Arrange
        val email = "test@test.com"
        val password = "password"


        coEvery { firestoreService.nicknameExists(any()) } returns true

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result is Result.Error && result.error == FirebaseError.Authentication.USERNAME_ALREADY_IN_USE)
//        assert(result.exceptionOrNull()?.message == "Username already in use")
    }

    @Test
    fun `When authService signUp fails, returns failure`() = runTest {
        // Arrange
        val email = "test@test.com"
        val password = "password"

        coEvery { firestoreService.nicknameExists(any()) } returns false
        coEvery { authService.signUp(email, password) } throws Exception()

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result is Result.Error && result.error == FirebaseError.Authentication.ACCOUNT_WITH_THAT_EMAIL_ALREADY_EXISTS)
    }

    @Test
    fun `When db insertion fails, returns failure`() = runTest {
        // Arrange
        val email = "test@test.com"
        val password = "password"

        coEvery { firestoreService.nicknameExists(any()) } returns false
        coEvery { authService.signUp(email, password) } returns firebaseUser
        coEvery { firestoreService.insertUser(any()) } returns Result.Error(FirebaseError.Firestore.INSERTION_ERROR)

        // Act
        val result = repositoryImpl.signUp(email, password, user1_test.nickname, user1_test.firstname, user1_test.lastname, user1_test.actor, user1_test.profession)

        // Assert
        assert(result is Result.Error)
    }

}