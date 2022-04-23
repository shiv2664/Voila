package com.android.testproject1.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.utils.Utils
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.HashMap
import java.util.regex.Pattern
import com.google.firebase.auth.FirebaseAuthException
import com.android.testproject1.MainActivity
import com.android.testproject1.sealedclasses.LoginUiStates


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val myTAG:String="MyTag"

    var profileImage:MutableLiveData<String> = MutableLiveData()

    private val authAppRepository: Repository = application.let { Repository(it) }
    private val userLiveData: MutableLiveData<FirebaseUser> = authAppRepository.getUserLiveData()
//private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val detailsRegisteredData: MutableLiveData<Boolean> = authAppRepository.getDetailsRegisteredData()

    val uiState=MutableLiveData<LoginUiStates>()


    fun validateEmailAddress(login_emailid: TextView): Boolean {
        val email = login_emailid.text.toString().trim { it <= ' ' }
        return if (email.isEmpty()) {
            login_emailid.error = "Email is required. Can't be empty."
            Toasty.error(getApplication(), "Email is required. Can't be empty.", Toasty.LENGTH_SHORT, true).show()
//            Toast.makeText(getApplication(), "Email is required. Can't be empty.", Toast.LENGTH_SHORT).show()
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_emailid.error = "Invalid Email. Enter valid email address."
            false
        } else {
            login_emailid.error = null
            true
        }
    }

    fun validatePassword(login_password: TextView): Boolean {
        val password1 = login_password.text.toString().trim { it <= ' ' }
        return if (password1.isEmpty()) {
            login_password.error = "Password is required. Can't be empty."
            false
        } else if (password1.length < 6) {
            login_password.error = "Password short. Minimum 6 characters required."
            false
        } else {
            login_password.error = null
            true
        }
    }

    private fun checkValidation(fullName:TextView,emailId:TextView,mobileNumber:TextView,location:TextView,
                                password:TextView,confirmPassword:TextView) {

        // Get all edittext texts
        val getFullName = fullName.text.toString()
        val getEmailId = emailId.text.toString()
        val getMobileNumber = mobileNumber.text.toString()
        val getLocation = location.text.toString()
        val getPassword = password.text.toString()
        val getConfirmPassword = confirmPassword.text.toString()

        // Pattern match for email id
        val p = Pattern.compile(Utils.regEx)
        val m = p.matcher(getEmailId)

        // Check if all strings are null or not
        if (getFullName == "" || getFullName.isEmpty() || getEmailId == "" || getEmailId.isEmpty()
            || getMobileNumber == "" || getMobileNumber.isEmpty() || getLocation == "" ||
            getLocation.isEmpty() || getPassword == "" || getPassword.isEmpty() ||
            getConfirmPassword == "" || getConfirmPassword.isEmpty()) {

            //            Toast.makeText(activity, "All fields are required", Toast.LENGTH_SHORT).show()
        } else if (!m.find()) emailId.error = "Your Email Id is Invalid" else if (getConfirmPassword != getPassword) {
            password.error = "Both password doesn't match."
            password.error = "Both password doesn't match."
        }
//        else if (!terms_conditions!!.isChecked) {
////            Toast.makeText(activity, "Please select Terms and Conditions.", Toast.LENGTH_SHORT).show()
//        }
//        else
////            Toast.makeText(activity, "Do SignUp.", Toast.LENGTH_SHORT)
////                .show()
//                    }

    }

    fun login(email: String, password: String){
        var sharedPrefDynamic: SharedPreferences
        uiState.value=LoginUiStates.Loading

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    uiState.value=LoginUiStates.Success()

                    firebaseFirestore.collection("Users").document(firebaseAuth.currentUser?.uid!!)
                        .get().addOnSuccessListener {
                            sharedPrefDynamic= getApplication<Application>().getSharedPreferences(firebaseAuth.currentUser?.uid!!,Context.MODE_PRIVATE)!!
                            val dynamicEditor= sharedPrefDynamic.edit()
                            if (it.getString("profileimage").toString().isNotBlank()) {
                                profileImage.postValue(it.getString("profileimage").toString())
                                Log.d(myTAG, "profileImage inside $profileImage")
                                it.getString("profileimage").toString()
                                dynamicEditor?.putString("profileimage", it.getString("profileimage").toString())
                                dynamicEditor?.apply()
                                userLiveData.postValue(firebaseAuth.currentUser)
                                userLiveData.postValue(firebaseAuth.currentUser)
                            }
                            userLiveData.postValue(firebaseAuth.currentUser)

                        }.addOnFailureListener {
                        Log.d(myTAG," "+it.localizedMessage)
                        }

//                    userLiveData.postValue(firebaseAuth.currentUser)

                    try{

                        FirebaseMessaging.getInstance().token.addOnCompleteListener {
                            if(it.isComplete){
                                if (it.isSuccessful){
                                    val tokenId = it.result.toString()
                                    Log.d(myTAG, tokenId)
                                    val currentId = firebaseAuth.currentUser?.uid.toString()
                                    firebaseFirestore.collection("Tokens").document(currentId).get().addOnSuccessListener {
                                        if (it.exists()){

                                            val tokenMap2: MutableMap<String, Any> = HashMap()
                                            tokenMap2["id"] = tokenId
                                            firebaseFirestore.collection("Tokens")
                                                .document(currentId)
                                                .update(tokenMap2)
                                                .addOnSuccessListener(OnSuccessListener<Void?> {
                                                    Log.d(myTAG, "token Updated")

                                                }).addOnFailureListener(OnFailureListener {
                                                    Log.d(myTAG, "token NotUpdated")
                                                    Log.d(myTAG, ""+it.localizedMessage?.toString())
                                                })
                                        }else if (!it.exists()){

                                            val tokenMap2: MutableMap<String, Any> = HashMap()
                                            tokenMap2["id"] = tokenId
                                            firebaseFirestore.collection("Tokens")
                                                .document(currentId)
                                                .set(tokenMap2)
                                                .addOnSuccessListener(OnSuccessListener<Void?> {
                                                    Log.d(myTAG, "token Updated")

                                                }).addOnFailureListener(OnFailureListener {
                                                    Log.d(myTAG, "token NotUpdated")
                                                    Log.d(myTAG, ""+it.localizedMessage?.toString())
                                                })

                                        }

                                    }
                                }
                                else{
                                    Log.d(myTAG,"error is "+" ")
                                }
                            }
                        }

                    }catch (e:Exception){
                        Log.d(myTAG,"Exception is : "+e.localizedMessage)
                    }

//                    FirebaseMessaging.getInstance().token.addOnCompleteListener {
//                        if(it.isComplete){
//                            val tokenId = it.result.toString()
//                            Log.d(myTAG, tokenId)
//                            val currentId = firebaseAuth.currentUser?.uid.toString()
//                            firebaseFirestore.collection("Tokens").document(currentId).get().addOnSuccessListener {
//                                val tokenMap2: MutableMap<String, Any> = HashMap()
//                                tokenMap2["id"] = tokenId
//                                firebaseFirestore.collection("Tokens")
//                                    .document(currentId)
//                                    .update(tokenMap2)
//                                    .addOnSuccessListener(OnSuccessListener<Void?> {
//                                        Log.d(myTAG, "token Updated")
//
//                                    }).addOnFailureListener(OnFailureListener {
//                                        Log.d(myTAG, "token NotUpdated")
//                                        Log.d(myTAG, it.localizedMessage.toString())
//                                    }) }
//                        }
//                    }


                } else if (!task.isSuccessful) {

                    uiState.value=LoginUiStates.Error()

                    when ((task.exception as FirebaseAuthException?)!!.errorCode) {
                        "ERROR_INVALID_CUSTOM_TOKEN" -> Toast.makeText(getApplication(),
                            "The custom token format is incorrect. Please check the documentation."
                            ,Toast.LENGTH_LONG).show()
                        "ERROR_CUSTOM_TOKEN_MISMATCH" -> Toast.makeText(getApplication(),
                            "The custom token corresponds to a different audience."
                            , Toast.LENGTH_LONG).show()
                        "ERROR_INVALID_CREDENTIAL" -> Toast.makeText(
                            getApplication(),
                            "The supplied auth credential is malformed or has expired.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_INVALID_EMAIL" -> {
                            Toast.makeText(
                                getApplication(),
                                "The email address is badly formatted.",
                                Toast.LENGTH_LONG
                            ).show()
//                            etEmail.setError("The email address is badly formatted.")
//                            etEmail.requestFocus()
                        }
                        "ERROR_WRONG_PASSWORD" -> {
                            Toast.makeText(
                                getApplication(),
                                "The password is invalid or the user does not have a password.",
                                Toast.LENGTH_LONG
                            ).show()
//                            etPassword.setError("password is incorrect ")
//                            etPassword.requestFocus()
//                            etPassword.setText("")
                        }
                        "ERROR_USER_MISMATCH" -> Toast.makeText(
                            getApplication(),
                            "The supplied credentials do not correspond to the previously signed in user.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_REQUIRES_RECENT_LOGIN" -> Toast.makeText(
                            getApplication(),
                            "This operation is sensitive and requires recent authentication. Log in again before retrying this request.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> Toast.makeText(
                            getApplication(),
                            "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_EMAIL_ALREADY_IN_USE" -> {
                            Toast.makeText(
                                getApplication(),
                                "The email address is already in use by another account.   ",
                                Toast.LENGTH_LONG
                            ).show()
//                            etEmail.setError("The email address is already in use by another account.")
//                            etEmail.requestFocus()
                        }
                        "ERROR_CREDENTIAL_ALREADY_IN_USE" -> Toast.makeText(
                            getApplication(),
                            "This credential is already associated with a different user account.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_USER_DISABLED" -> Toast.makeText(
                            getApplication(),
                            "The user account has been disabled by an administrator.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_USER_TOKEN_EXPIRED" -> Toast.makeText(
                            getApplication(),
                            "The user\\'s credential is no longer valid. The user must sign in again.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_USER_NOT_FOUND" -> Toast.makeText(
                            getApplication(),
                            "There is no user record corresponding to this identifier. The user may have been deleted.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_INVALID_USER_TOKEN" -> Toast.makeText(
                            getApplication(),
                            "The user\\'s credential is no longer valid. The user must sign in again.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_OPERATION_NOT_ALLOWED" -> Toast.makeText(
                            getApplication(),
                            "This operation is not allowed. You must enable this service in the console.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_WEAK_PASSWORD" -> {
                            Toast.makeText(
                                getApplication(),
                                "The given password is invalid.",
                                Toast.LENGTH_LONG
                            ).show()
//                            etPassword.setError("The password is invalid it must 6 characters at least")
//                            etPassword.requestFocus()
                        }
                    }



//                    try {
//                        throw task.exception!!
//                    } catch (e: FirebaseAuthWeakPasswordException) {
//                        Log.d(myTAG, e.message!!+"FirebaseAuthWeakPasswordException")
//                        Toasty.error(getApplication(), "Weak Password", Toasty.LENGTH_SHORT, true).show()
//                    } catch (e: FirebaseAuthInvalidCredentialsException) {
//                        Log.d(myTAG, e.message!!+"FirebaseAuthInvalidCredentialsException")
//                        Toasty.error(getApplication(), "Invalid Credentials", Toasty.LENGTH_SHORT, true).show()
//                    } catch (e: FirebaseAuthUserCollisionException) {
//                        Log.d(myTAG, e.message!!+"FirebaseAuthUserCollisionException")
//                        Toasty.error(getApplication(), "User already exists", Toasty.LENGTH_SHORT, true).show()
//                    } catch (e: Exception) {
//                        Log.d(myTAG, e.message!!)
//                    }catch (e: FirebaseAuthEmailException){
//                        Log.d(myTAG, e.message!!+"FirebaseAuthEmailException")
//                        Toasty.error(getApplication(), "Invalid Email", Toasty.LENGTH_SHORT, true).show()
//                    } catch (e: FirebaseAuthInvalidUserException){
//                        Log.d(myTAG, e.message!!+"FirebaseAuthInvalidUserException")
//                        Toasty.error(getApplication(), "Invalid User", Toasty.LENGTH_SHORT, true).show()
//                    }
                }

            }
    }

//    @JvmName("detailsRegisteredCheck")
//    fun getDetailsRegisteredData(): MutableLiveData<Boolean> {
//        return detailsRegisteredData
//    }

    @JvmName("getUserLiveData1")
    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }

    @JvmName("getUserProfileImage")
    fun getUserProfileImage(): MutableLiveData<String> {
        return profileImage
    }


}