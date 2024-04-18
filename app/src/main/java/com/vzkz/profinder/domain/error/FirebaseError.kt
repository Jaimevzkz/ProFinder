package com.vzkz.profinder.domain.error

sealed interface FirebaseError: Error {
    enum class Authentication: FirebaseError {
        WRONG_EMAIL_OR_PASSWORD,
        USERNAME_ALREADY_IN_USE,
        ACCOUNT_WITH_THAT_EMAIL_ALREADY_EXISTS,
        UNKNOWN_ERROR,

    }
    enum class Firestore: FirebaseError {
        USER_NOT_FOUND_IN_DATABASE,
        CONNECTION_ERROR,
        NICKNAME_IN_USE,
        MODIFICATION_ERROR,
        INSERTION_ERROR,
        NON_EXISTENT_USER_FIELD,
        NONEXISTENT_SERVICE_ATTRIBUTE,
        NONEXISTENT_REQUEST_ATTRIBUTE,
        UNKNOWN_ERROR,
        CORRUPTED_DATABASE_DATA,
        DELETION_ERROR,
        REQUEST_ADDITION_ERROR,
        JOB_ADDITION_ERROR,
        LOCATION_UPDATE_ERROR
    }

    enum class Realtime: FirebaseError {
        NULL_REALTIME_USERDATA,
        RECENT_CHAT_UPDATE_ERROR,
        ERROR_GETTING_RECENT_CHATS,
        REALTIME_ACCESS_INTERRUPTED,
        UNKNOWN_ERROR,

    }

    enum class Storage: FirebaseError {
        PHOTO_DELETION_ERROR,
        UNKNOWN_ERROR,

    }
}