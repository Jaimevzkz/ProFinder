package com.vzkz.profinder.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.vzkz.profinder.core.Constants.PROFILEPHOTOS
import com.vzkz.profinder.domain.error.DataError
import com.vzkz.profinder.domain.error.Result
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class StorageService @Inject constructor(private val storage: FirebaseStorage) {

    suspend fun uploadAndDownloadProgressPhoto(uri: Uri, uid: String, oldProfileUri: Uri?): Uri {
        if(oldProfileUri != null)
            deletePhoto(oldProfileUri)
        return suspendCancellableCoroutine { cancellableContinuation ->
            val reference = storage.reference.child("$PROFILEPHOTOS/$uid/${uri.lastPathSegment}")
            reference.putFile(uri).addOnSuccessListener {
                downloadImage(it, cancellableContinuation)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }

    private fun deletePhoto(uri: Uri): Result<Unit, DataError.Storage>{
        try {
            val reference = storage.reference.child("${uri.lastPathSegment}")
            reference.delete().addOnSuccessListener {
                Log.i("Jaime","Photo deleted correctly")
            }.addOnFailureListener {
                Log.e("Jaime","Error deleting photo")
                throw Exception()
            }
        } catch (e: Exception){
            return Result.Error(DataError.Storage.PHOTO_DELETION_ERROR)
        }
        return Result.Success(Unit)
    }


    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot, cancellableContinuation: CancellableContinuation<Uri>
    ) {
        uploadTask.storage.downloadUrl
            .addOnSuccessListener { uri -> cancellableContinuation.resume(uri) }
            .addOnFailureListener { cancellableContinuation.resumeWithException(it) }
    }

    suspend fun getProfilePhoto(uid: String): Uri?{
        val reference = storage.reference.child("$PROFILEPHOTOS/$uid")
        return reference.listAll().await().items.firstOrNull()?.downloadUrl?.await()
    }

}