package com.android.testproject1.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.lottie.LottieAnimationView
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentProfileOpenedBinding
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.UserImagesRoomEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.marcoscg.dialogsheet.DialogSheet
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap
import android.content.Intent
import android.net.Uri
import android.app.Activity





class ProfileOpened : Fragment() {

    private lateinit var binding: FragmentProfileOpenedBinding
    private lateinit var mFirestore:FirebaseFirestore
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var remove_friend:Button
    private lateinit var add_friend:Button
    private lateinit var req_sent:TextView
    private lateinit var req_layout:LinearLayout

    private var currentUserID:String?=null
    private var id: String?=null

    private var friend_name:String? = null
    private var friend_email:String? = null
    private var friend_image:String? = null
    private var friend_token:String? = null
    private lateinit var sharedPrefDynamic:SharedPreferences
    private lateinit var userId:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentProfileOpenedBinding.inflate(inflater,container,false)

        mFirestore= FirebaseFirestore.getInstance()
        mFirebaseAuth= FirebaseAuth.getInstance()
        currentUserID=mFirebaseAuth.currentUser?.uid

        val localDatabase: AppDatabase? = AppDatabase.getInstance(requireActivity())
//        val sharedPref= activity?.getSharedPreferences("currentUserDetails", Context.MODE_PRIVATE)
//        val editor = sharedPref?.edit()


//        if (currentUserID!=null){
//            sharedPrefDynamic= activity?.getSharedPreferences(currentUserID,Context.MODE_PRIVATE)!!
//        }

//        val offerItem=arguments?.getParcelable<OfferRoomEntity>("offerItem")
        val userIdBundle=arguments?.getString("userId")
        userId = userIdBundle ?: currentUserID.toString()

        if(currentUserID!=userId){
            binding.ln1.visibility=View.GONE
            binding.ln2.visibility=View.GONE

            binding.ln1Opened.visibility=View.VISIBLE
            binding.ln2Opened.visibility=View.VISIBLE
        }

        sharedPrefDynamic= activity?.getSharedPreferences(userId,Context.MODE_PRIVATE)!!
        Log.d("MyTag", "profile image is: "+sharedPrefDynamic.getString("profileimage",""))
        Log.d("MyTag","name is : "+sharedPrefDynamic?.getString("username",""))
        val dynamicEditor= sharedPrefDynamic?.edit()
        var firstTimeCheck= sharedPrefDynamic?.getInt("firstTimeCheck",0)
        firstTimeCheck++
        dynamicEditor?.putInt("firstTimeCheck",firstTimeCheck)
        dynamicEditor?.apply()

        binding.CallOpened.setOnClickListener {
            val number: Uri = Uri.parse("tel:"+sharedPrefDynamic.getString("profilenumber",""))
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            startActivity(callIntent)
        }

//        var firstTimeCheck= sharedPref?.getInt("firstTimeCheck",0)
//        if (firstTimeCheck!=null){
//            firstTimeCheck++
//            editor?.putInt("firstTimeCheck",firstTimeCheck)
//            editor?.apply()
//        }

//        val bundle2 = this.arguments
//        if (bundle2 != null) {
//            id = bundle2.getString("iD")
////            Log.d("MyTag", "id Profile Opened  is not null : $id")
//        } else {
//            Toasty.error(requireActivity(), "Error retrieving information.", Toasty.LENGTH_SHORT, true).show()
//            requireActivity().finish()
//        }
//        Log.d("MyTag", "id Profile Opened  is : $id")



        id=userId

        req_sent= binding.friendSent
        remove_friend= binding.friendYes
        add_friend=binding.friendNo
        req_layout=binding.friendReq

//        val reference=FirebaseFirestore.getInstance()
//        val fUser=FirebaseAuth.getInstance().currentUser?.uid
//        reference.collection("Users").document(fUser.toString()).get().addOnSuccessListener {
//            if (it != null) {
//                if (it.exists()) {
//                    // convert document to POJO
//                    val notifPojo: Users? = it.toObject(Users::class.java)
//
//                    if (notifPojo != null) {
//                        if (notifPojo.profileimage.isNotEmpty()) {
//                            activity?.let { it1 ->
//                                Glide.with(it1)
//                                    .load(notifPojo.profileimage)
//                                    .into(binding.imageView)
//                            }
//                        }
////                        Log.d("MyTag"," Image Url is "+notifPojo.profileimage)
//                    }
//                }
//            }
//
//        }

        binding.sendMessage.setOnClickListener {

        }

        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileOpened_to_editProfileFragment)
        }
        binding.editImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileOpened_to_editProfileFragment)
        }

        binding.saved.setOnClickListener {
            findNavController().navigate(R.id.action_profileOpened_to_viewPagerFragmentSaved)
        }
        binding.savedImage.setOnClickListener {
            findNavController().navigate(R.id.action_profileOpened_to_viewPagerFragmentSaved)
        }

        binding.Offers
            .setOnClickListener {

//                val bundle = Bundle()
//                bundle.putString("currentUserId",currentUserID)

                findNavController().navigate(R.id.action_profileOpened_to_viewPagerFragmentCurrentUserPosts)
            }

        if (firstTimeCheck!=1){

//            binding.name.text = sharedPref?.getString("userName","")
//            binding.email.text = sharedPref?.getString("userEmail","")
//            binding.bio.text = sharedPref?.getString("userBio","")
//            Glide.with(requireActivity())
//                .load(sharedPref?.getString("profileimage",""))
//                .into(binding.imageView)
            binding.name.text = sharedPrefDynamic?.getString("username","")
            binding.email.text = sharedPrefDynamic?.getString("userEmail","")
            binding.bio.text = sharedPrefDynamic?.getString("userBio","")
            Glide.with(requireActivity())
                .load(sharedPrefDynamic?.getString("profileimage",""))
                .addListener(imageLoadingListener(binding.lottiViewProfile))
                .into(binding.imageView)
        }

        mFirestore.collection("Users")
            .document(id!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->

                if (firstTimeCheck==1){

                    binding.name.text = documentSnapshot.getString("username")
                    binding.email.text = documentSnapshot.getString("email")
                    binding.bio.text = documentSnapshot.getString("bio")
                    val activity: Activity? = activity
                    Glide.with(requireActivity())
                        .load(documentSnapshot.getString("profileimage"))
                        .addListener(imageLoadingListener(binding.lottiViewProfile))
                        .into(binding.imageView)
                }

                val userImageRoomEntity: UserImagesRoomEntity? = documentSnapshot
                    .toObject(UserImagesRoomEntity::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    if (userImageRoomEntity != null) {
                        localDatabase?.appDao()?.insertImage(userImageRoomEntity)
                    }
                }

//                    editor?.putString("userName",documentSnapshot.getString("name"))
//                    editor?.putString("userEmail",documentSnapshot.getString("email"))
//                    editor?.putString("profileimage",documentSnapshot.getString("profileimage"))
//                    editor?.putString("userBio",documentSnapshot.getString("bio"))
//                    editor?.apply()
                dynamicEditor?.putString("username",documentSnapshot.getString("username"))
                dynamicEditor?.putString("userEmail",documentSnapshot.getString("email"))
                dynamicEditor?.putString("profileimage",documentSnapshot.getString("profileimage"))
                dynamicEditor?.putString("profilenumber",documentSnapshot.getString("phoneNumber"))
                dynamicEditor?.putString("userBio",documentSnapshot.getString("bio"))
                dynamicEditor?.apply()


            }


//        currentUserID?.let {
//            id?.let { it1 ->
//                mFirestore.collection("Users")
//                    .document(it)
//                    .collection("Friends")
//                    .document(it1)
//                    .get()
//                    .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
//                        if (documentSnapshot.exists()) {
//                            req_sent.visibility = View.GONE
//                            showRemoveButton()
//                        } else {
//                            mFirestore.collection("Users")
//                                .document(id!!)
//                                .collection("Friend_Requests")
//                                .document(currentUserID!!)
//                                .get()
//                                .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
//                                    if (!documentSnapshot.exists()) {
//                                        mFirestore.collection("Users")
//                                            .document(currentUserID!!)
//                                            .collection("Friend_Requests")
//                                            .document(id!!)
//                                            .get()
//                                            .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
//                                                if (documentSnapshot.exists()) {
//                                                    req_sent.setVisibility(View.GONE)
//                                                    showRequestLayout()
//                                                } else {
//                                                    req_sent.setVisibility(View.GONE)
//                                                    showAddButton()
//                                                }
//                                            })
//                                            .addOnFailureListener(OnFailureListener { e ->
//                                                Log.w(
//                                                    "error",
//                                                    "fail",
//                                                    e
//                                                )
//                                            })
//                                    } else {
//                                        req_sent.text = "Friend request sent"
//                                        req_sent.visibility = View.VISIBLE
//                                        req_sent.alpha = 0.0f
//                                        req_sent.animate()
//                                            .setDuration(200)
//                                            .alpha(1.0f)
//                                            .start()
//                                    }
//                                })
//                                .addOnFailureListener(OnFailureListener { e -> Log.w("error", "fail", e) })
//                        }
//                    })
//                    .addOnFailureListener(OnFailureListener { e -> Log.w("error", "fail", e) })
//            }
//        }

        return binding.root
    }

    fun imageLoadingListener(pendingImage: LottieAnimationView): RequestListener<Drawable?>? {
        return object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                pendingImage.pauseAnimation()
                pendingImage.visibility = View.GONE
                return false
            }
        }
    }


    private fun showRequestLayout() {
        req_layout.visibility = View.VISIBLE
        req_layout.alpha = 0.0f
        req_layout.animate()
            .setDuration(200)
            .alpha(1.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.accept.setOnClickListener(View.OnClickListener { v: View? ->

                        MaterialDialog.Builder(requireActivity())
                            .title("Accept Friend Request")
                            .content("Are you sure do you want to accept friend_name's friend request?")
                            .positiveText("Yes")
                            .negativeText("No")
                            .onPositive { dialog, which ->
                                acceptRequest()
                            }
                            .onNegative { dialog, which ->
                                dialog.dismiss()
                            }
                            .cancelable(true)
                            .show()
                    })
                    binding.decline.setOnClickListener(View.OnClickListener { v: View? ->
                        DialogSheet(requireActivity())
                            .setTitle("Decline Friend Request")
                            .setMessage("Are you sure do you want to decline name's friend request?")
                            .setPositiveButton(
                                "Yes"
                            ) { v13: View? -> declineRequest() }
                            .setNegativeButton("No") { v14: View? -> }
                            .setRoundedCorners(true)
                            .setColoredNavigationBar(true)
                            .setCancelable(true)
                            .show()
                    })
                }
            }).start()
    }


    private fun showRemoveButton() {
        remove_friend.visibility = View.VISIBLE
        remove_friend.alpha = 0.0f
        remove_friend.animate()
            .setDuration(200)
            .alpha(1.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    remove_friend.setOnClickListener(View.OnClickListener { v: View? ->

                        MaterialDialog.Builder(requireActivity())
                            .title("Remove Friend ")
                            .content("Are you sure do you want to remove friend_name from your friend list?")
                            .positiveText("Yes")
                            .negativeText("No")
                            .onPositive { dialog, which ->
                                removeFriend()
                            }
                            .onNegative { dialog, which ->
                                dialog.dismiss()
                            }
                            .cancelable(true)
                            .show()
                    })
                }
            }).start()
    }

    fun removeFriend() {
        currentUserID?.let {
            mFirestore.collection("Users").document(it)
                .collection("Friends").document(id!!).delete().addOnSuccessListener {
                    FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(id!!)
                        .collection("Friends")
                        .document(currentUserID!!)
                        .delete()
                        .addOnSuccessListener {
                            Toasty.success(requireActivity(), "Friend removed successfully", Toasty.LENGTH_SHORT, true).show()
                            remove_friend.animate()
                                .alpha(0.0f)
                                .setDuration(200)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        super.onAnimationEnd(animation)
                                        remove_friend.visibility = View.GONE
                                        showAddButton()
                                    }
                                }).start()
                        }
                }.addOnFailureListener { e -> Log.e("Error", e.message!!) }
        }
    }

    private fun showAddButton() {
        add_friend.setVisibility(View.VISIBLE)
        add_friend.setAlpha(0.0f)
        add_friend.animate()
            .setDuration(200)
            .alpha(1.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    add_friend.setOnClickListener(View.OnClickListener { v: View? ->

                        MaterialDialog.Builder(requireActivity())
                            .title("Add Friend ")
                            .content("Are you sure do you want to send friend request to friend_name ?")
                            .positiveText("Yes")
                            .negativeText("No")
                            .cancelable(false)
                            .onPositive { dialog, which ->
                                addFriend()
                            }
                            .onNegative { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                    })
                }
            }).start()
    }

    fun addFriend() {
        currentUserID?.let {
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(id!!)
                .collection("Friends")
                .document(it)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(id!!)
                            .collection("Friend_Requests")
                            .document(currentUserID!!)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (!documentSnapshot.exists()) {
                                    executeFriendReq()
                                } else {
                                    add_friend.animate()
                                        .alpha(0.0f)
                                        .setDuration(200)
                                        .setListener(object : AnimatorListenerAdapter() {
                                            override fun onAnimationEnd(animation: Animator) {
                                                super.onAnimationEnd(animation)
                                                add_friend.visibility = View.GONE
                                                req_sent.visibility = View.VISIBLE
                                                req_sent.alpha = 0.0f
                                                req_sent.animate()
                                                    .setDuration(200)
                                                    .alpha(1.0f)
                                                    .start()
                                            }
                                        }).start()
                                    Toasty.info(
                                       requireActivity(),
                                        "Friend request has been sent already",
                                        Toasty.LENGTH_SHORT,
                                        true
                                    ).show()
                                }
                            }
                    }
                }
        }
    }

    private fun executeFriendReq() {
        val userMap: MutableMap<String, Any?> = HashMap()
        currentUserID?.let {
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(it)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val email = documentSnapshot.getString("email")
                    userMap["name"] = documentSnapshot.getString("name")
                    userMap["id"] = documentSnapshot.getString("id")
                    userMap["email"] = email
//                    userMap["image"] = documentSnapshot.getString("image")
//                    userMap["tokens"] = documentSnapshot["token_ids"]
                    userMap["notification_id"] = System.currentTimeMillis().toString()
                    userMap["timestamp"] = System.currentTimeMillis().toString()

                    //Add to user
                    FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(id!!)
                        .collection("Friend_Requests")
                        .document(documentSnapshot.getString("id")!!)
                        .set(userMap)
                        .addOnSuccessListener {

                            addToNotification(id!!, currentUserID!!,
//                                        documentSnapshot.getString("image"),
                                        documentSnapshot.getString("name")!!,
                                        "Sent you friend request",
                                        "friend_req")
                            //Add for notification data
//
                        //                            FirebaseFirestore.getInstance()
//                                .collection("Notifications")
//                                .document(id!!)
//                                .collection("Friend_Requests")
//                                .document(email!!)
//                                .set(userMap)
//                                .addOnSuccessListener {
//                                    addToNotification(id!!, currentUserID!!,
////                                        documentSnapshot.getString("image"),
//                                        documentSnapshot.getString("name")!!,
//                                        "Sent you friend request",
//                                        "friend_req")
//                                }.addOnFailureListener { e -> Log.e("Error", e.message!!) }

                        }.addOnFailureListener { e -> Log.e("Error", e.message!!) }
                }
        }
    }

    private fun addToNotification(
        admin_id: String,
        currentUserId: String,
//        profile: String="",
        name: String,
        message: String,
        type: String
    ) {
        val map: MutableMap<String, Any> = HashMap()
        map["id"] = currentUserId
        map["name"] = name
//        map["image"] = profile
        map["message"] = message
        map["timestamp"] = System.currentTimeMillis().toString()
        map["type"] = type
        map["action_id"] = currentUserId
        if (admin_id != currentUserId) {
            mFirestore.collection("Users")
                .document(admin_id)
                .collection("Info_Notifications")
                .add(map)
                .addOnSuccessListener { documentReference: DocumentReference? ->
                    if (type == "friend_req") {
                        req_sent.text = "Friend request sent"
                        Toasty.success(
                            requireActivity(),
                            "Friend request sent.",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                        add_friend.animate()
                            .alpha(0.0f)
                            .setDuration(200)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    add_friend.visibility = View.GONE
                                    req_sent.visibility = View.VISIBLE
                                    req_sent.alpha = 0.0f
                                    req_sent.animate()
                                        .setDuration(200)
                                        .alpha(1.0f)
                                        .start()
                                }
                            }).start()
                    } else {
//                        mDialog.dismiss()
                        Toasty.success(
                            requireActivity(),
                            "Friend request accepted",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                        req_layout.animate()
                            .alpha(0.0f)
                            .setDuration(200)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    req_layout.setVisibility(View.GONE)
                                    showRemoveButton()
                                }
                            }).start()
                    }
                }
                .addOnFailureListener { e: Exception ->
                    Log.e(
                        "Error",
                        e.localizedMessage
                    )
                }
        }
    }

    fun acceptRequest() {
//        mDialog.show()

        //Delete from friend request
        currentUserID?.let {
            mFirestore.collection("Users")
                .document(it)
                .collection("Friend_Requests")
                .document(id!!)
                .delete()
                .addOnSuccessListener {
                    val friendInfo: MutableMap<String, String> = HashMap()
                    friendInfo["name"] = friend_name!!
                    friendInfo["email"] = friend_email!!
                    friendInfo["id"] = id!!
//                    friendInfo["image"] = friend_image!!
//                    friendInfo["token_ids"] = friend_tokens
                    friendInfo["notification_id"] = System.currentTimeMillis().toString()
                    friendInfo["timestamp"] = System.currentTimeMillis().toString()

                    //Add data friend to current user
                    mFirestore.collection("Users/$currentUserID/Friends/")
                        .document(id!!)
                        .set(friendInfo)
                        .addOnSuccessListener {
                            //get the current user data
                            mFirestore.collection("Users")
                                .document(currentUserID!!)
                                .get()
                                .addOnSuccessListener { documentSnapshot ->
                                    val name_c = documentSnapshot.getString("name")
                                    val email_c = documentSnapshot.getString("email")
                                    val id_c = documentSnapshot.id
//                                    val image_c = documentSnapshot.getString("image")
                                    val username_c = documentSnapshot.getString("username")
                                    val tokens_c =
                                        documentSnapshot["token_ids"] as List<String>?
                                    val currentuserInfo: MutableMap<String, Any?> =
                                        HashMap()
                                    currentuserInfo["name"] = name_c
                                    currentuserInfo["email"] = email_c
                                    currentuserInfo["id"] = id_c
//                                    currentuserInfo["image"] = image_c
                                    currentuserInfo["token_ids"] = tokens_c
                                    currentuserInfo["notification_id"] =
                                        System.currentTimeMillis().toString()
                                    currentuserInfo["timestamp"] =
                                        System.currentTimeMillis().toString()

                                    //Save current user data to Friend
                                    mFirestore.collection("Users/$id/Friends/")
                                        .document(id_c)
                                        .set(currentuserInfo)
                                        .addOnSuccessListener {
                                            mFirestore.collection("Notifications")
                                                .document(id!!)
                                                .collection("Accepted_Friend_Requests")
                                                .document(email_c!!)
                                                .set(currentuserInfo)
                                                .addOnSuccessListener {
                                                    addToNotification(
                                                        id!!,
                                                        id_c,
//                                                        image_c!!,
                                                        username_c!!,
                                                        "Accepted your friend request",
                                                        "accept_friend_req"
                                                    )
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("Error", e.message!!)
                                                }
                                        }.addOnFailureListener { e ->
//                                            mDialog.dismiss()
                                            Log.w("fourth", "listen:error", e)
                                        }
                                }.addOnFailureListener { e ->
//                                    mDialog.dismiss()
                                    Log.w("third", "listen:error", e)
                                }
                        }.addOnFailureListener { e ->
//                            mDialog.dismiss()
                            Log.w("second", "listen:error", e)
                        }
                }.addOnFailureListener { e ->
//                    mDialog.dismiss()
                    Log.w("first", "listen:error", e)
                }
        }
    }

    private fun declineRequest() {
        try {
            //delete friend request data
            currentUserID?.let {
                mFirestore.collection("Users").document(it)
                    .collection("Friend_Requests").document(id!!).delete().addOnSuccessListener {
                        Toasty.success(requireActivity(), "Friend request denied", Toasty.LENGTH_SHORT, true).show()
                        req_layout.animate()
                            .alpha(0.0f)
                            .setDuration(200)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    req_layout.visibility = View.GONE
                                    showAddButton()
                                }
                            }).start()
                    }.addOnFailureListener { e -> Log.i("Error decline", e.message!!) }
            }
        } catch (ex: Exception) {
            Log.w("error", "fail", ex)
            Toasty.error(requireActivity(), "Some technical error occurred while declining friend request, Try again later."
                , Toasty.LENGTH_SHORT, true).show()
        }
    }

}