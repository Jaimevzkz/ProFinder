package com.vzkz.profinder.domain.usecases.location

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetLocationUseCase {
    suspend operator fun invoke(): Flow<LatLng?>
    suspend fun updateFirestoreLocation(): Result<Unit, FirebaseError.Firestore>}


class GetLocationUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUserUseCase: GetUserUseCase
): GetLocationUseCase {
    @RequiresApi(Build.VERSION_CODES.S)
    override suspend operator fun invoke(): Flow<LatLng?> = repository.getLocation()

    @RequiresApi(Build.VERSION_CODES.S)
    override suspend fun updateFirestoreLocation(): Result<Unit, FirebaseError.Firestore> {
        return when(val user = getUserUseCase()){
            is Result.Success -> {
                repository.updateFirestoreLocation(
                    uid = user.data.uid,
                    nickname = user.data.nickname
                )
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(user.error)
        }

    }

}