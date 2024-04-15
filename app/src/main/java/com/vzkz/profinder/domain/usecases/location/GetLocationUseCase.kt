package com.vzkz.profinder.domain.usecases.location

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetLocationUseCase {
    suspend operator fun invoke(): Flow<LatLng?>}


class GetLocationUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
): GetLocationUseCase {
    @RequiresApi(Build.VERSION_CODES.S)
    override suspend operator fun invoke(): Flow<LatLng?> = repository.getLocation(getUidDataStoreUseCase())

}