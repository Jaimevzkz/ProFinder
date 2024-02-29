package com.vzkz.profinder.domain.model

object Constants {
    //User
    const val USERS_COLLECTION = "users"
    const val NICKNAME = "nickname"
    const val FIRSTNAME = "firstname"
    const val LASTNAME = "lastname"
    const val DESCRIPTION = "description"
    const val IS_USER = "isUser"
    const val PROFESSION = "profession"
    const val STATE = "state"
    const val FAVOURITES = "favourites"

    //Services
    const val SERVICES_COLLECTION = "services"
    const val UID = "uid"
    const val NAME = "name"
    const val IS_ACTIVE = "isActive"
    const val CATEGORY = "category"
    const val SERV_DESCRIPTION = "description"
    const val PRICE = "price"

    //Storage
    const val PROFILEPHOTO = "profilePhoto"
    const val PROFILEPHOTOS = "ProfilePhotos"


    //Errors
    const val ERRORSTR = "error"
    const val VALUENOTSET = "value not set"
    const val ERRORINT = -1

    //Exceptions
    const val CONNECTION_ERROR = "CONNECTION_FAILURE"
    const val UNKNOWN_EXCEPTION = "UNKNOWN_EXCEPTION"
    const val NULL_USERDATA = "NULL_USERDATA"
    const val NONEXISTENT_USERFIELD = "NONEXISTENT_USERDATA"
    const val NICKNAME_IN_USE = "NICKNAME_IN_USE"
    const val MODIFICATION_ERROR = "MODIFICATION_ERROR"
    const val INSERTION_ERROR = "INSERTION_ERROR"
    const val NONEXISTENT_SERVICEATTRIBUTE = "NONEXISTENT_SERVICEATTRIBUTE"

}