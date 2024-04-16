package com.vzkz.profinder.ui

import com.vzkz.profinder.R
import com.vzkz.profinder.domain.error.DataError


fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Authentication.WRONG_EMAIL_OR_PASSWORD -> UiText.StringResource(R.string.wrong_email_or_password)

        DataError.Authentication.USERNAME_ALREADY_IN_USE -> UiText.StringResource(R.string.username_already_in_use)

        DataError.Authentication.ACCOUNT_WITH_THAT_EMAIL_ALREADY_EXISTS -> UiText.StringResource(R.string.account_already_exists)

        DataError.Authentication.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)

        DataError.Firestore.USER_NOT_FOUND_IN_DATABASE -> UiText.StringResource(R.string.error_logging_user)

        DataError.Firestore.CONNECTION_ERROR -> UiText.StringResource(R.string.network_failure_while_checking_user_existence)

        DataError.Firestore.NICKNAME_IN_USE -> UiText.StringResource(R.string.nickname_already_in_use_couldn_t_modify_user)

        DataError.Firestore.MODIFICATION_ERROR -> UiText.StringResource(R.string.error_modifying_user_data_the_user_wasn_t_modified)

        DataError.Firestore.INSERTION_ERROR -> UiText.StringResource(R.string.couldn_t_insert_user_in_database)

        DataError.Firestore.NON_EXISTENT_USER_FIELD -> UiText.StringResource(R.string.needed_values_missing_in_database)

        DataError.Firestore.NONEXISTENT_SERVICE_ATTRIBUTE -> UiText.StringResource(R.string.attribute_of_a_service_corrupted_in_the_database)

        DataError.Firestore.NONEXISTENT_REQUEST_ATTRIBUTE -> UiText.StringResource(R.string.an_attribute_of_a_request_was_corrupted_in_the_database)

        DataError.Firestore.CORRUPTED_DATABASE_DATA -> UiText.StringResource(R.string.corrupted_db_data)

        DataError.Firestore.DELETION_ERROR -> UiText.StringResource(R.string.deletion_error)

        DataError.Firestore.REQUEST_ADDITION_ERROR -> UiText.StringResource(R.string.request_addition_error)

        DataError.Firestore.JOB_ADDITION_ERROR -> UiText.StringResource(R.string.job_addition_error)

        DataError.Firestore.LOCATION_UPDATE_ERROR -> UiText.StringResource(R.string.location_update_error)

        DataError.Realtime.NULL_REALTIME_USERDATA -> UiText.StringResource(R.string.realtime_data_was_corrupted)

        DataError.Realtime.REALTIME_ACCESS_INTERRUPTED -> UiText.StringResource(R.string.access_to_realtime_database_was_interrupted)

        DataError.Realtime.RECENT_CHAT_UPDATE_ERROR -> UiText.StringResource(R.string.recent_chat_update_error)

        DataError.Realtime.ERROR_GETTING_RECENT_CHATS ->UiText.StringResource(R.string.recent_chat_get_error)

        DataError.Storage.PHOTO_DELETION_ERROR ->UiText.StringResource(R.string.account_already_exists)

        DataError.Firestore.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)

        DataError.Realtime.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)

        DataError.Storage.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error_occurred)
    }
}
//fun Result<*, DataError>.asErrorUiText(): UiText