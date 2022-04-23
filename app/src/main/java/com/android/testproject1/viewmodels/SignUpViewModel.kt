package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import java.util.*

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val myTAG:String="MyTag"

    private val authAppRepository: Repository = application.let { Repository(it) }
    private val userLiveData: MutableLiveData<FirebaseUser> = authAppRepository.getUserLiveData()
    private val detailsRegisteredData: MutableLiveData<Boolean> = authAppRepository.getDetailsRegisteredData()

    fun validateEmailAddress(login_emailid: TextView): Boolean {
        val email = login_emailid.text.toString().trim { it <= ' ' }
        return if (email.isEmpty()) {
            login_emailid.error = "Email is required. Can't be empty."
//            Toasty.error(getApplication(), "Email is required. Can't be empty.", Toasty.LENGTH_SHORT, true).show()
//            Toast.makeText(getApplication(), "Email is required. Can't be empty.", Toast.LENGTH_SHORT).show()
//            Toast.makeText(getApplication(),)
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

    fun register(email: String, password: String, username: String, phoneNumber: String=""){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    userLiveData.postValue(firebaseAuth.currentUser)

                    val uid= firebaseAuth.currentUser?.uid.toString()
                    val userMap: MutableMap<String, Any> = HashMap()
                    val postMap: MutableMap<String, Any> = HashMap()
                    val offerMap: MutableMap<String, Any> = HashMap()
                    val chatListMap: MutableMap<String, Any> = HashMap()

                    userMap["userId"] = uid
                    userMap["username"] = username
                    userMap["email"] = email
                    userMap["bio"] = ""
                    userMap["location"] = ""
                    userMap["phoneNumber"]=phoneNumber
                    postMap["Posts"]= uid
                    offerMap["Offers"]= uid
                    chatListMap["Owner"]=uid


                    firebaseFirestore.collection("Users").document(uid).set(userMap)
                        .addOnSuccessListener {
                            detailsRegisteredData.postValue(true)
                            firebaseFirestore.collection("Users").document(uid)
                                .collection("Posts").document("UploadedPosts")
                                .set(postMap).continueWith {
                                    firebaseFirestore.collection("ChatList").document(uid)
                                        .set(chatListMap, SetOptions.merge()).continueWith {
                                            firebaseFirestore.collection("Users").document(uid)
                                                .collection("Offers").document("UploadedOffers")
                                                .set(offerMap)
                                        }
//                                        .continueWith {
//                                            firebaseFirestore.collection("Users").document(uid)
//                                                .collection("Friends").document("UserFriends")
//                                                .set(chatListMap)
//                                        }
                                }

                            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                                if(it.isComplete){
                                    val tokenId = it.result.toString()
//                                    Log.d(myTAG, tokenId)
                                    val currentId = firebaseAuth.currentUser?.uid.toString()

                                    firebaseFirestore.collection("Tokens").document(currentId)
                                        .get().addOnSuccessListener {
                                        val tokenMap2: MutableMap<String, Any> = HashMap()
                                        tokenMap2["id"] = tokenId
                                        firebaseFirestore.collection("Tokens")
                                            .document(currentId)
                                            .set(tokenMap2)
                                            .addOnSuccessListener(OnSuccessListener<Void?> {
                                                userLiveData.postValue(firebaseAuth.currentUser)
//                                                Log.d(myTAG, "token Updated")

                                            }).addOnFailureListener(OnFailureListener {
                                                Log.d(myTAG, "token NotUpdated")
                                                Log.d(myTAG, ""+it.localizedMessage)

                                            })
                                    }
                                }
                            }


                        }.addOnFailureListener {

                            Log.d("MyTag",""+it.localizedMessage)
                        }

                } else if (!task.isSuccessful) {


                    when ((task.exception as FirebaseAuthException?)!!.errorCode) {
                        "ERROR_INVALID_CUSTOM_TOKEN" -> Toast.makeText(getApplication(),
                            "The custom token format is incorrect. Please check the documentation."
                            , Toast.LENGTH_LONG).show()
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

    @JvmName("getUserLiveData1")
    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }



}