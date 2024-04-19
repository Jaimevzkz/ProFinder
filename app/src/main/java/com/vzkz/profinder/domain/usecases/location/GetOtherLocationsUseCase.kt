package com.vzkz.profinder.domain.usecases.location

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.LocationModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetOtherLocationsUseCase {
    suspend operator fun invoke(): Result<Flow<List<LocationModel>>, FirebaseError.Firestore>
}


class GetOtherLocationsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : GetOtherLocationsUseCase {
    override suspend operator fun invoke(): Result<Flow<List<LocationModel>>, FirebaseError.Firestore> =
        repository.getLocations(uid = getUidDataStoreUseCase())

}