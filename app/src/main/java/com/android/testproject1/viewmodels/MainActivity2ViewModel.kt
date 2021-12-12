package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.android.testproject1.Repository
import com.android.testproject1.model.Offer
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import es.dmoral.toasty.Toasty
import java.lang.Exception


class MainActivity2ViewModel(application: Application) : AndroidViewModel(application) {
    private var firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseFirestore:FirebaseFirestore = FirebaseFirestore.getInstance()
    val myTag="MyTag"
    var idOrder:String?=null

    fun onPlaceOrder(offerItem: Offer, Total: String, Quantity: String){
        Log.d(myTag, "Total is$Total")
        Log.d(myTag, "Quantity is $Quantity")
        val currentUserId: String = firebaseAuth.currentUser?.uid!!

        MaterialDialog.Builder(getApplication())
            .title("Confirm")
            .content("Confirm order request?")
            .positiveText("Yes")
            .negativeText("No")
            .cancelable(true)
            .canceledOnTouchOutside(false)
            .neutralText("Cancel")
            .onPositive { _, _ ->
                addOrder(offerItem.userId,currentUserId,offerItem.title,Total,Quantity)
//                    binding.refreshLayout.isRefreshing=true
//                mViewModel.deleteAll()
//                    binding.refreshLayout.isRefreshing=false
            }
            .onNegative { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun addOrder(userItem: String, currentUserId: String,itemName:String,price: String,Quantity: String){
        if (userItem!=currentUserId) {
            executeOrder(userItem, currentUserId, itemName, price, Quantity)
        }else{
            Toasty.error(getApplication(),"Error", Toasty.LENGTH_SHORT,true).show()
        }
    }

    private fun executeOrder(userItem: String, currentUserId: String,itemName:String,price: String,Quantity: String){

        idOrder= firebaseFirestore.collection("Users")
            .document(userItem)
            .collection("Orders").document().id

        val messageText:String="Order Request"
        val type:String ="order_req"

        val userMap = HashMap<String, Any?>()
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val email = documentSnapshot.getString("email")
                userMap["username"] = documentSnapshot.getString("name")
                userMap["id"] = documentSnapshot.getString("id")
                userMap["email"] = email
                userMap["image"] = documentSnapshot.getString("image")
//                userMap["tokens"] = documentSnapshot["token_ids"]
                userMap["notification_id"] = System.currentTimeMillis().toString()
                userMap["orderName"] =itemName
                userMap["status"] ="Pending"
                userMap["message"] =messageText
                userMap["type"] =type
                userMap["price"]=price
                userMap["Quantity"]=Quantity
                userMap["orderFrom"]=currentUserId
                userMap["orderTo"]=userItem
                userMap["idOrder"]=idOrder
                userMap["timestamp"] = FieldValue.serverTimestamp()

//               idOrder= firebaseFirestore.collection("Users")
//                    .document(userItem)
//                    .collection("Orders").document().id
                //Add to user


                firebaseFirestore
                    .collection("Users")
                    .document(userItem)
                    .collection("Orders")
//                    .document(currentUserId)
                    .document(idOrder!!)
                    .set(userMap, SetOptions.merge()).continueWith {
                        firebaseFirestore
                            .collection("Users")
                            .document(currentUserId)
                            .collection("Orders")
//                    .document(currentUserId)
                            .document(idOrder!!).set(userMap, SetOptions.merge())
                    }
                    .addOnSuccessListener {

                        firebaseFirestore
                            .collection("Users")
                            .document(userItem)
                            .collection("Orders")
                            .document("recentOrder").set(userMap).continueWith {
                                firebaseFirestore
                                    .collection("Users")
                                    .document(currentUserId)
                                    .collection("Orders")
                                    .document("recentOrder").set(userMap)
                            }

                        documentSnapshot.getString("name")?.let { it1 ->
                            addToNotification(userItem, currentUserId,
                                //                                documentSnapshot.getString("image"),
                                it1,
                                messageText,
                                type,itemName,price,Quantity
                            )
                        }

//                        Toasty.success(getApplication(),"Order Request Sent",Toasty.LENGTH_SHORT).show()
                    }.addOnFailureListener { e: java.lang.Exception ->
//                        holder.progressBar.setVisibility(View.GONE)
                        Log.e("Error", e.message!!)
                    }
            }

    }


    private fun addToNotification(
        UserItemId: String,
        currentUserId: String,
//        profile: String,
        username: String,
        message: String,
        type: String,
        itemName: String="",
        price:String="",
        Quantity: String="1"
    ){

        val map: MutableMap<String, Any> = java.util.HashMap()
        map["id"] = currentUserId
        map["username"] = username
//        map["image"] = profile
        map["message"] = message
        map["timestamp"] = FieldValue.serverTimestamp()
        map["type"] = type
        map["action_id"] = currentUserId
        if(type=="order_req"){
            map["orderName"] =itemName
            map["status"] ="Pending"
            map["price"]=price
            map["orderFrom"]=currentUserId
            map["orderTo"]=UserItemId
            map["idOrder"]= idOrder.toString()
            map["Quantity"]=Quantity
        }
        if (UserItemId != currentUserId) {

            if (type=="order_req"){

                idOrder?.let {
                    firebaseFirestore.collection("Users")
                        .document(UserItemId)
                        .collection("Info_Notifications")
                        .document(it)
                        .set(map, SetOptions.merge()).addOnSuccessListener {

                            Toasty.success(getApplication(), "Order request Sent.", Toasty.LENGTH_SHORT,true).show()
                        }
                        .addOnFailureListener { e: Exception ->
                            Log.e("Error",""+ e.localizedMessage)
                        }
                }

            }else{

                firebaseFirestore.collection("Users")
                    .document(UserItemId)
                    .collection("Info_Notifications")
                    .document(idOrder!!)
                    .set(map, SetOptions.merge()).addOnSuccessListener {
                        Toasty.success(getApplication(), "Friend request Sent.", Toasty.LENGTH_SHORT,true).show()
                    }.addOnFailureListener {
                        Log.e("Error",""+it.localizedMessage)
                    }
//                    .addOnSuccessListener(OnSuccessListener { documentReference: DocumentReference? ->
//                        if (type == "friend_req") {
////                        req_sent.setText("Friend request sent")
//                            Toasty.success(this, "Friend request Sent.", Toasty.LENGTH_SHORT,true).show()
//                        }
////                    else if (type == "order_req"){
////                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
////                    }
////                    else { Toasty.success(this, "Friend request accepted", Toasty.LENGTH_SHORT, true).show()
////                    }
////                    if (type == "order_req") {
//////                        req_sent.setText("Friend request sent")
////                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
////                    }
////                    else { Toasty.success(this, "Order request accepted", Toasty.LENGTH_SHORT, true).show()
////                    }
//                    })
//                    .addOnFailureListener(OnFailureListener { e: java.lang.Exception ->
//                        Log.e("Error", e.localizedMessage)
//                    })

            }
//            firebaseFirestore.collection("Users")
//                .document(UserItemId)
//                .collection("Info_Notifications")
//                .add(map)
//                .addOnSuccessListener(OnSuccessListener { documentReference: DocumentReference? ->
//                    if (type == "friend_req") {
////                        req_sent.setText("Friend request sent")
//                        Toasty.success(this, "Friend request sent.", Toasty.LENGTH_SHORT,true).show()
//                    }
////                    else if (type == "order_req"){
////                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
////                    }
////                    else { Toasty.success(this, "Friend request accepted", Toasty.LENGTH_SHORT, true).show()
////                    }
////                    if (type == "order_req") {
//////                        req_sent.setText("Friend request sent")
////                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
////                    }
////                    else { Toasty.success(this, "Order request accepted", Toasty.LENGTH_SHORT, true).show()
////                    }
//                })
//                .addOnFailureListener(OnFailureListener { e: java.lang.Exception ->
//                    Log.e("Error", e.localizedMessage)
//                })
        }

    }

}



