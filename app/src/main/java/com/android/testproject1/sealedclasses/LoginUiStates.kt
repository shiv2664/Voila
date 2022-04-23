package com.android.testproject1.sealedclasses

sealed class LoginUiStates {

    data class Success(val data:String=""):LoginUiStates(){}
    data class Error(val message:String=""):LoginUiStates(){}
    object Loading:LoginUiStates(){}

}