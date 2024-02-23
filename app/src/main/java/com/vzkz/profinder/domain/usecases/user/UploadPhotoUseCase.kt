package com.vzkz.profinder.domain.usecases.user

import android.net.Uri
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton
import javax.inject.Inject

interface UploadPhotoUseCase {
    suspend operator fun invoke(uri: Uri, user: ActorModel): Uri
}

class UploadPhotoUseCaseImpl @Inject constructor(private val repository: Repository) :
    UploadPhotoUseCase {

    private val userInstance = UserDataSingleton.getUserInstance(repository)
    private val serviceListSingleton = ServiceListSingleton.getServiceListInstance(repository)
    override suspend operator fun invoke(uri: Uri, user: ActorModel): Uri =
        repository.uploadAndDownloadProfilePhoto(
            uri = uri,
            uid = user.uid,
            oldProfileUri = user.profilePhoto
        ).also { storageUri ->
            userInstance.setProfilePhoto(storageUri)
            if (user.actor == Actors.Professional)
                serviceListSingleton.changeServiceListOwnerProfilePic(storageUri)
        }
}