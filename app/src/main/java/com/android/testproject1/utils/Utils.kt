package com.android.testproject1.utils

import java.util.regex.Pattern

object Utils {
    //Email Validation pattern
    const val regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b"

    //Fragments Tags
    const val Login_Fragment = "Login_Fragment"
    const val SignUp_Fragment = "SignUp_Fragment"
    const val ForgotPassword_Fragment = "ForgotPassword_Fragment"
    val PASSWORD_PATTERN = Pattern.compile("^"
            + "(?=.*[0-9])" //minimum one number
            + "(?=.*[a-z])" //minimum one lower case character
            + "(?=.*[A-Z])" //minimum one UPPER case character
            + "(?=.*[a-zA-Z])" //any character
            + "(?=.*[@#$%^&+=])" //minimum one special character
            + "(?=\\S+$)" //no white spaces
            + ".{6,}" //minimum length 6 characters
            + "$")
    val PASSWORD_UPPERCASE_PATTERN = Pattern.compile("(?=.*[A-Z])" + ".{0,}")
    val PASSWORD_LOWERCASE_PATTERN = Pattern.compile("(?=.*[a-z])" + ".{0,}")
    val PASSWORD_NUMBER_PATTERN = Pattern.compile("(?=.*[0-9])" + ".{0,}")
    val PASSWORD_SPECIAL_CHARACTER_PATTERN = Pattern.compile("(?=.*[@#$%^&+=])" + ".{0,}")
}