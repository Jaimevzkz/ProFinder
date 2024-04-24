package com.vzkz.profinder.ui.components


fun validateEmail(email: String): Boolean =
    email.matches(Regex("[a-zA-Z0-9.+_-]+@[a-z]+\\.+[a-z]+"))

fun validatePassword(password: String) =
    validateMinimum(password) && validateCapitalizedLetter(password)

private fun validateCapitalizedLetter(password: String): Boolean =
    password.matches(Regex(".*[A-Z].*"))

private fun validateMinimum(password: String): Boolean =
    password.length > 6
