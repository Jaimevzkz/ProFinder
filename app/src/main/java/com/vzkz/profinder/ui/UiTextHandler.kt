package com.vzkz.profinder.ui

import com.vzkz.profinder.R
import com.vzkz.profinder.domain.error.FirebaseError


fun FirebaseError.asUiText(): UiText {
    return when (this) {
        FirebaseError.Authentication.WRONG_EMAIL_OR_PASSWORD -> UiText.StringResource(R.string.wrong_email_or_password)

        FirebaseError.Authentication.USERNAME_ALREADY_IN_USE -> UiText.StringResource(R.string.username_already_in_use)

        FirebaseError.Authentication.ACCOUNT_WITH_THAT_EMAIL_ALREADY_EXISTS -> UiText.StringResource(R.string.account_already_exists)

        FirebaseError.Authentication.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)

        FirebaseError.Firestore.USER_NOT_FOUND_IN_DATABASE -> UiText.StringResource(R.string.error_logging_user)

        FirebaseError.Firestore.CONNECTION_ERROR -> UiText.StringResource(R.string.network_failure_while_checking_user_existence)

        FirebaseError.Firestore.NICKNAME_IN_USE -> UiText.StringResource(R.string.nickname_already_in_use_couldn_t_modify_user)

        FirebaseError.Firestore.MODIFICATION_ERROR -> UiText.StringResource(R.string.error_modifying_user_data_the_user_wasn_t_modified)

        FirebaseError.Firestore.INSERTION_ERROR -> UiText.StringResource(R.string.couldn_t_insert_user_in_database)

        FirebaseError.Firestore.NON_EXISTENT_USER_FIELD -> UiText.StringResource(R.string.needed_values_missing_in_database)

        FirebaseError.Firestore.NONEXISTENT_SERVICE_ATTRIBUTE -> UiText.StringResource(R.string.attribute_of_a_service_corrupted_in_the_database)

        FirebaseError.Firestore.NONEXISTENT_REQUEST_ATTRIBUTE -> UiText.StringResource(R.string.an_attribute_of_a_request_was_corrupted_in_the_database)

        FirebaseError.Firestore.CORRUPTED_DATABASE_DATA -> UiText.StringResource(R.string.corrupted_db_data)

        FirebaseError.Firestore.DELETION_ERROR -> UiText.StringResource(R.string.deletion_error)

        FirebaseError.Firestore.REQUEST_ADDITION_ERROR -> UiText.StringResource(R.string.request_addition_error)

        FirebaseError.Firestore.JOB_ADDITION_ERROR -> UiText.StringResource(R.string.job_addition_error)

        FirebaseError.Firestore.LOCATION_UPDATE_ERROR -> UiText.StringResource(R.string.location_update_error)

        FirebaseError.Firestore.ERROR_GETTING_LOCATIONS -> UiText.StringResource(R.string.error_getting_location)

        FirebaseError.Firestore.ERROR_GETTING_USERS -> UiText.StringResource(R.string.error_getting_users)

        FirebaseError.Realtime.NULL_REALTIME_DATA -> UiText.StringResource(R.string.realtime_data_was_corrupted)

        FirebaseError.Realtime.REALTIME_ACCESS_INTERRUPTED -> UiText.StringResource(R.string.access_to_realtime_database_was_interrupted)

        FirebaseError.Realtime.RECENT_CHAT_UPDATE_ERROR -> UiText.StringResource(R.string.recent_chat_update_error)

        FirebaseError.Realtime.ERROR_GETTING_RECENT_CHATS ->UiText.StringResource(R.string.recent_chat_get_error)

        FirebaseError.Storage.PHOTO_DELETION_ERROR ->UiText.StringResource(R.string.account_already_exists)

        FirebaseError.Firestore.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)

        FirebaseError.Realtime.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)

        FirebaseError.Storage.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)
    }
}
//fun Result<*, DataError>.asErrorUiText(): UiText