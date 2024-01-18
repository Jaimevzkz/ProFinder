package com.vzkz.profinder.domain.model

object Constants {
    //User
    const val USERS_COLLECTION = "users"
    const val NICKNAME = "nickname"
    const val FIRSTNAME = "firstname"
    const val LASTNAME = "lastname"
    const val DESCRIPTION = "description"
    const val ISUSER = "isUser"
    const val PROFESSION = "profession"
    const val STATE = "state"

    //Errors
    const val ERRORSTR = "error"
    const val ERRORINT = -1

    //Exceptions
    const val CONNECTION_ERROR = "CONNECTION_FAILURE"
    const val UNKNOWN_EXCEPTION = "UNKNOWN_EXCEPTION"
    const val NULL_USERDATA = "NULL_USERDATA"
    const val NICKNAME_IN_USE = "NICKNAME_IN_USE"
    const val MODIFICATION_ERROR = "MODIFICATION_ERROR"

}