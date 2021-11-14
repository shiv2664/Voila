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

    fun register(email: String, password: String, fullName: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)

                    val uid= firebaseAuth.currentUser?.uid.toString()
                    val userMap: MutableMap<String, Any> = HashMap()
                    val postMap: MutableMap<String, Any> = HashMap()
                    val offerMap: MutableMap<String, Any> = HashMap()
                    val chatListMap: MutableMap<String, Any> = HashMap()

                    userMap["id"] = uid
                    userMap["name"] = fullName
//                      userMap["image"] = uri.toString()
                    userMap["email"] = email
                    userMap["bio"] = ""
                    userMap["username"] = ""
                    userMap["location"] = ""

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
//                                                Log.d(myTAG, "token Updated")

                                            }).addOnFailureListener(OnFailureListener {
                                                Log.d(myTAG, "token NotUpdated")
                                                Log.d(myTAG, it.localizedMessage.toString())

                                            })
                                    }
                                }
                            }


                        }.addOnFailureListener {

                        }

                }

                else if (!task.isSuccessful) {
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

    @JvmName("getUserLiveData1")
    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }



}