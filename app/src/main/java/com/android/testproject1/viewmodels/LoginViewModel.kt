package com.android.testproject1.viewmodels

import android.app.Application
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


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val myTAG:String="MyTag"

    private val authAppRepository: Repository = application.let { Repository(it) }
    private val userLiveData: MutableLiveData<FirebaseUser> = authAppRepository.getUserLiveData()
//private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val detailsRegisteredData: MutableLiveData<Boolean> = authAppRepository.getDetailsRegisteredData()

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

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)

                    try{

                        FirebaseMessaging.getInstance().token.addOnCompleteListener {
                            if(it.isComplete){
                                if (it.isSuccessful){
                                    val tokenId = it.result.toString()
                                    Log.d(myTAG, tokenId)
                                    val currentId = firebaseAuth.currentUser?.uid.toString()
                                    firebaseFirestore.collection("Tokens").document(currentId).get().addOnSuccessListener {
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
                                            }) }
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
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        Log.d(myTAG, e.message!!+"FirebaseAuthWeakPasswordException")
                        Toasty.error(getApplication(), "Weak Password", Toasty.LENGTH_SHORT, true).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Log.d(myTAG, e.message!!+"FirebaseAuthInvalidCredentialsException")
                        Toasty.error(getApplication(), "Invalid Credentials", Toasty.LENGTH_SHORT, true).show()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        Log.d(myTAG, e.message!!+"FirebaseAuthUserCollisionException")
                        Toasty.error(getApplication(), "User already exists", Toasty.LENGTH_SHORT, true).show()
                    } catch (e: Exception) {
                        Log.d(myTAG, e.message!!)
                    }catch (e: FirebaseAuthEmailException){
                        Log.d(myTAG, e.message!!+"FirebaseAuthEmailException")
                        Toasty.error(getApplication(), "Invalid Email", Toasty.LENGTH_SHORT, true).show()
                    } catch (e: FirebaseAuthInvalidUserException){
                        Log.d(myTAG, e.message!!+"FirebaseAuthInvalidUserException")
                        Toasty.error(getApplication(), "Invalid User", Toasty.LENGTH_SHORT, true).show()
                    }
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


}