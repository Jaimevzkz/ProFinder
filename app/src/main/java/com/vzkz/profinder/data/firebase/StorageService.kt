package com.vzkz.profinder.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.vzkz.profinder.domain.model.Constants.PROFILEPHOTO
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
            val reference = storage.reference.child("$uid/$PROFILEPHOTO/${uri.lastPathSegment}")
            reference.putFile(uri).addOnSuccessListener {
                downloadImage(it, cancellableContinuation)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }

    fun deletePhoto(uri: Uri){
        val reference = storage.reference.child("${uri.lastPathSegment}")
        reference.delete().addOnSuccessListener {
            Log.i("Jaime","Photo deleted correctly")
        }.addOnFailureListener {
            Log.e("Jaime","Error deleting photo")
        }
    }


    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot, cancellableContinuation: CancellableContinuation<Uri>
    ) {
        uploadTask.storage.downloadUrl
            .addOnSuccessListener { uri -> cancellableContinuation.resume(uri) }
            .addOnFailureListener { cancellableContinuation.resumeWithException(it) }
    }

    suspend fun getProfilePhoto(uid: String): Uri?{
        val reference = storage.reference.child("$uid/$PROFILEPHOTO")
        return reference.listAll().await().items.firstOrNull()?.downloadUrl?.await()
    }

}