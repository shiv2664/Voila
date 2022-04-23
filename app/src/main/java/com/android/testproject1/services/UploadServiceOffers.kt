package com.android.testproject1.services

import android.R
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.nguyenhoanglam.imagepicker.model.Image
import es.dmoral.toasty.Toasty
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import com.google.firebase.firestore.DocumentReference





class UploadServiceOffers : Service() {


    companion object{
        const val ACTION_START_FOREGROUND_SERVICE_UPLOAD_OFFERS = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_START_FOREGROUND_SERVICE_UPLOAD_PROFILE_PIC = "ACTION_START_FOREGROUND_SERVICE_PROFILE_PIC"
    }
    private var count = 0
    private var bitmap: Bitmap? = null
    private var imageBitmap: Bitmap? = null
    private var resized: Bitmap? = null
    val myTag:String = "MyTag"

    private var uploadedProfileUrl = ArrayList<String>()

    var numberImages:Int=0


    private val job = SupervisorJob()
    private lateinit var scope : CoroutineScope


    override fun onCreate() {
        super.onCreate()
        Log.d(myTag, "Service Created ")
        scope=CoroutineScope(Dispatchers.Main + job)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(myTag, "onStart $intent   $flags  $startId")
        Log.d(myTag, "onStartCommand intent is : ${intent?.action}")

        if (intent!=null) {
            val action = intent.action
            if (action == ACTION_START_FOREGROUND_SERVICE_UPLOAD_OFFERS) {

                val imagesList: ArrayList<Image>? = intent.getParcelableArrayListExtra<Image>("imagesList")

                val notificationId = intent.getIntExtra("notification_id", 3)

                val postID = intent.getStringExtra("offerId")

                val title=intent.getStringExtra("title")

                val minPeople=intent.getStringExtra("minPeople")

                val originalPrice=intent.getStringExtra("originalPrice")

                val discountedPrice=intent.getStringExtra("discountedPrice")

                val currentId = intent.getStringExtra("current_id")

                val description = intent.getStringExtra("description")

                val uploadedImagesUrl = intent.getStringArrayListExtra("uploadedImagesUrl")

                val city=intent.getStringExtra("city")

                count = intent.getIntExtra("count", 0)


//                Log.d(myTag, "image list is $imagesList notificationId is $notificationId " +
//                        "postID is $postID title is $title originalPrice is $originalPrice " +
//                        "discountedPrice is $discountedPrice city is $city currentId is $currentId " +
//                        " description is $description uploadedImagesUrl is $uploadedImagesUrl")

                if (imagesList != null) {
                    if (postID != null) {
                        if (title != null) {
                            if (originalPrice != null) {
                                if (discountedPrice != null) {
                                    if (city != null) {
                                        if (minPeople != null) {
                                            uploadImages(notificationId, 0, imagesList, currentId, description,
                                                uploadedImagesUrl, postID,title,originalPrice,discountedPrice,city,minPeople)
                                        }

                                        Log.d(myTag,"Nothing is null")
                                    }
                                }
                            }
                        }
                    }
                }

            }else if(action == ACTION_START_FOREGROUND_SERVICE_UPLOAD_PROFILE_PIC){

                Log.d("MyTag", "Action UPLOAD_PROFILE_PIC ")
                val imageUri= Uri.parse(intent.getStringExtra("imageUri"))
                uploadProfilePic(File(imageUri.path!!))

            }

        }


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        job.cancel()
        stopSelf()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun stopForegroundService(removeNotification: Boolean) {

        Log.d(myTag,"Stop foreground service.")
        // Stop foreground service and remove the notification.
        stopForeground(removeNotification)
        // Stop the foreground service.
        stopSelf()
    }

    private fun notifyProgress(
        id: Int,
        icon: Int,
        title: String,
        message: String,
        context: Context,
        max_progress: Int,
        progress: Int,
        indeterminate: Boolean
    ) {
        val builder = NotificationCompat.Builder(context, App.CHANNEL_ID2)
//         Create notification default intent.
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        builder.setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setTicker(message)
            .setChannelId(App.CHANNEL_ID2)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setProgress(max_progress, progress, indeterminate)
            .setVibrate(LongArray(0))
        startForeground(id, builder.build())

    }


//    fun getImageUri(inContext: Context, inImage: Bitmap) {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        var path: String?=null
//        try {
//            path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
//
//        }catch (e: java.lang.Exception){
//            e.localizedMessage?.toString()?.let { Log.d(myTag, it) }
//        }
//        if (path!=null){
//            Log.d(myTag, Uri.parse(path).toString())
//        }
//        else{
//            Log.d(myTag, "Path is null ")
//        }
//
//
//    }

    private fun uploadProfilePic(imageList: File){

        scope.launch{
            val compressedImageFile= imageList.let {
                Compressor.compress(application, imageList){
//                    default(150,150,Bitmap.CompressFormat.WEBP,80)
                    default(100,100,format = Bitmap.CompressFormat.WEBP, quality = 80)

                }
            }

//            val compressedThumbnailFile=imageList.let {
//                Compressor.compress(application,imageList){
//                    default(50,50,Bitmap.CompressFormat.WEBP, 80)
//                }
//            }

            val userId= FirebaseAuth.getInstance().currentUser?.uid
            uploadProfilePicsCoroutine(compressedImageFile,compressedImageFile,4,1,userId,uploadedProfileUrl) }


    }

    private fun updateImageUrl(notification_id: Int,
                               currentUser_id: String?,
                               uploadedImagesUrl: ArrayList<String>?) {
        FirebaseFirestore.getInstance().collection("Users")
            .document(currentUser_id!!)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val postMap: MutableMap<String, Any?> = HashMap()
                val id = documentSnapshot.getString("id")

                postMap["profileimage"] = uploadedImagesUrl?.get(0).toString()
                postMap["thumbnailImage"] = uploadedImagesUrl?.get(1).toString()

                val firestore=FirebaseFirestore.getInstance()
//                val firebaseAuth=FirebaseAuth.getInstance()

                firestore.collection("Users")
                    .document(currentUser_id).update(postMap).addOnSuccessListener {
                        Toasty.success(applicationContext, "Profile Pic Uploaded", Toasty.LENGTH_SHORT, true).show()
                        val sharedPrefDynamic: SharedPreferences = getSharedPreferences(currentUser_id, Context.MODE_PRIVATE)!!
                        val dynamicEditor= sharedPrefDynamic.edit()
                        val profileImage= sharedPrefDynamic.getString("profileimage","")
                            dynamicEditor?.putString("profileimage",uploadedImagesUrl?.get(0).toString())
                            dynamicEditor?.apply()

//                        val batch: WriteBatch =firestore.batch()
//
//                        firestore.collection("Offers").whereEqualTo("userId",currentUser_id)
//                            .get().addOnSuccessListener {
//                                for (i in it){
//                                    val offerRoomEntity=i.toObjects(OfferRoomEntity::class.java)
//                                    firestore.collection("Offers").document(Offe)
//                                }
//
//                        }

                        stopForegroundService(true)

                    }.addOnFailureListener {
                        Log.d("MyTag",""+it.localizedMessage)
                        stopForegroundService(true)
                    }

            }
    }

    private fun uploadProfilePicsCoroutine(
        compressedImageFile: File, compressedThumbnailFile: File, notification_id: Int,
        index: Int,
        currentUser_id: String?,
        uploadedImagesUrl: ArrayList<String>?) {

        Log.d("MyTag", "Upload Pics Coroutine")
        Log.d("MyTag", "Index is : $index")
        var imageUri= Uri.fromFile(compressedImageFile)
        var imageUri2=Uri.fromFile(compressedThumbnailFile)
//        imageUri = if (index==1){
//            Uri.fromFile(compressedImageFile)
//        }else{
//            Uri.fromFile(compressedThumbnailFile)
//        }

//        if(index==1){
//           imageUri= Uri.fromFile(compressedImageFile)
////            imageUri= compressedImageFile
//        }else{
//          imageUri =  Uri.fromFile(compressedThumbnailFile)
////            imageUri =  compressedThumbnailFile
//        }

        val fileToUpload = currentUser_id?.let {
            FirebaseStorage.getInstance().reference.child(it).child("ProfilePic")
                .child("Voila_$index.webp")
        }

        if (index <=2) {
            if (index==1) {
                fileToUpload?.putFile(imageUri)?.addOnSuccessListener {
                    fileToUpload.downloadUrl
                        .addOnSuccessListener { uri: Uri ->
                            uploadedImagesUrl!!.add(uri.toString())
                            val nextIndex = index + 1
                            try {
                                if (nextIndex == 2) {
                                    uploadProfilePicsCoroutine(
                                        compressedImageFile,
                                        compressedThumbnailFile,
                                        notification_id,
                                        nextIndex,
                                        currentUser_id,
                                        uploadedImagesUrl
                                    )
                                } else if (nextIndex >= 3) {
                                    updateImageUrl(
                                        notification_id,
                                        currentUser_id,
                                        uploadedImagesUrl
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                updateImageUrl(
                                    notification_id,
                                    currentUser_id,
                                    uploadedImagesUrl
                                )
                            }
                        }.addOnFailureListener { obj: Exception -> obj.printStackTrace() }
                }?.addOnFailureListener { obj: Exception ->
                    obj.printStackTrace()
                    obj.localizedMessage?.toString()?.let { Log.d(myTag, "Exception is  $it") }
                }?.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
//                if (count == 1) {
//                    val title = " Updating "
//                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
//                    notifyProgress(notification_id, R.drawable.stat_sys_upload, title, "$progress%",
//                        applicationContext, 100, progress, true)
//                }
//                else if (count > 1)
//                {
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    notifyProgress(
                        notification_id,
                        R.drawable.stat_sys_upload,
                        "Viola",
                        "Updating ",
                        applicationContext,
                        100,
                        progress,
                        true
                    )
//                }
                }
            }else if(index==2){
                fileToUpload?.putFile(imageUri2)?.addOnSuccessListener {
                    fileToUpload.downloadUrl
                        .addOnSuccessListener { uri: Uri ->
                            uploadedImagesUrl!!.add(uri.toString())
                            val nextIndex = index + 1
                            try {
                                if (nextIndex == 2) {
                                    uploadProfilePicsCoroutine(
                                        compressedImageFile,
                                        compressedThumbnailFile,
                                        notification_id,
                                        nextIndex,
                                        currentUser_id,
                                        uploadedImagesUrl
                                    )
                                } else if (nextIndex >= 3) {
                                    updateImageUrl(
                                        notification_id,
                                        currentUser_id,
                                        uploadedImagesUrl
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                updateImageUrl(
                                    notification_id,
                                    currentUser_id,
                                    uploadedImagesUrl
                                )
                            }
                        }.addOnFailureListener { obj: Exception -> obj.printStackTrace() }
                }?.addOnFailureListener { obj: Exception ->
                    obj.printStackTrace()
                    obj.localizedMessage?.toString()?.let { Log.d(myTag, "Exception is  $it") }
                }?.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
//                if (count == 1) {
//                    val title = " Updating "
//                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
//                    notifyProgress(notification_id, R.drawable.stat_sys_upload, title, "$progress%",
//                        applicationContext, 100, progress, true)
//                }
//                else if (count > 1)
//                {
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    notifyProgress(
                        notification_id,
                        R.drawable.stat_sys_upload,
                        "Viola",
                        "Updating ",
                        applicationContext,
                        100,
                        progress,
                        true
                    )
//                }
                }
            }
        }else{
            updateImageUrl(
                notification_id,
                currentUser_id,
                uploadedImagesUrl)
        }

    }

    private fun uploadImages(
        notification_id: Int,
        index: Int,
        imagesList: ArrayList<Image>,
        currentUser_id: String?,
        description: String?,
        uploadedImagesUrl: ArrayList<String>?,
        postID:String,
        title: String,
        originalPrice:String,
        discountPrice:String,
        city:String,
        minPeople:String="1"
    ) {

        Log.d(myTag,"path is : "+File(imagesList[index].path))
        scope.launch {
            val compressedImageFile = Compressor.compress(applicationContext, File(imagesList[index].path)) {
//                    resolution(100, 100)
                default(300,300,Bitmap.CompressFormat.WEBP,80)
//                    quality(100)
//                    format(Bitmap.CompressFormat.WEBP)
            }
            uploadFromCoroutine(
                compressedImageFile, notification_id, index,imagesList
                ,currentUser_id,description, uploadedImagesUrl,postID, title,originalPrice,discountPrice, city, minPeople)

        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.WEBP, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver
            , inImage, "Title"+System.currentTimeMillis(), null)
        return Uri.parse(path)
    }


    private fun uploadFromCoroutine(compressedImageFile: File, notification_id: Int,
                                    index: Int,
                                    imagesList: ArrayList<Image>,
                                    currentUser_id: String?,
                                    description: String?,
                                    uploadedImagesUrl: ArrayList<String>?,
                                    postID:String,
                                    title: String,
                                    originalPrice:String,
                                    discountPrice:String,
                                    city:String,
                                    minPeople:String="1") {

        Log.d("MyTag","Fun launched : ")
        val imgCount = index + 1
        val imageUri0: Uri?= Uri.fromFile(compressedImageFile)
//        var imageUri: Uri?

//        if (Build.VERSION.SDK_INT >= 29) {
//            try {
//                bitmap = imageUri0?.let { ImageDecoder.createSource(this.contentResolver,it)}?.let { ImageDecoder.decodeBitmap(it) }
//            } catch (e: IOException) {
//                e.printStackTrace()
//
//                e.localizedMessage?.toString()?.let { Log.d(myTag, " error is  $it") }
//            }
//        } else {
//            // Use older version
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri0)
//            } catch (e: IOException) {
//                e.printStackTrace()
//                e.localizedMessage?.toString()?.let { Log.d(myTag, " error is  $it") }
//            }
//        }
//
////        resized = bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
//        resized=bitmap

        var path :String?=null

//        try {
////            path = MediaStore.Images.Media.insertImage(this.contentResolver, resized, "Title", null)
//            path = MediaStore.Images.Media.insertImage(this.contentResolver, resized,  "IMG_"
//                    + System.currentTimeMillis(), null)
//            Log.d(myTag, "path is  $path")
//
//        }catch (e :java.lang.Exception){
//            Log.d(myTag, "path is exception $path" )
//            Log.d(myTag, ""+e.localizedMessage?.toString() )
//
//        }

//        imageUri = Uri.parse(path)
//            imageUri = getImageUri(applicationContext,resized!!)

        val imageUri: Uri= Uri.fromFile(compressedImageFile)


        val fileToUpload = currentUser_id?.let { FirebaseStorage.getInstance().reference.child("Offers").child(it)
            .child(postID)
            .child("Voila_"+System.currentTimeMillis()+"_$numberImages")
        }
        numberImages++
        fileToUpload?.putFile(imageUri)?.addOnSuccessListener {
            Log.d(myTag, "Uploaded Successfully")
            fileToUpload.downloadUrl
                .addOnSuccessListener { uri: Uri ->
                    uploadedImagesUrl!!.add(uri.toString())
                    val nextIndex = index + 1
                    try {
                        if (!TextUtils.isEmpty(imagesList[index + 1].path)) {
                            uploadImages(
                                notification_id,
                                nextIndex,
                                imagesList,
                                currentUser_id,
                                description,
                                uploadedImagesUrl,
                                postID,
                                title,originalPrice, discountPrice,city,minPeople)
                        } else {
                            uploadPost(
                                notification_id,
                                currentUser_id,
                                description,
                                uploadedImagesUrl,
                                postID,
                                title,originalPrice,discountPrice,city,minPeople)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        uploadPost(
                            notification_id,
                            currentUser_id,
                            description,
                            uploadedImagesUrl, postID,
                            title, originalPrice, discountPrice,city,minPeople)
                    }
                }
                .addOnFailureListener { obj: Exception -> obj.printStackTrace() }
        }?.addOnFailureListener { obj: Exception ->
            obj.printStackTrace()
            obj.localizedMessage?.toString()?.let { Log.d(myTag, "Exception is  $it") }
        }?.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
            if (count == 1) {
                val title = "Uploading " + imgCount + "/" + imagesList.size + " images..."
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()

                notifyProgress(notification_id, R.drawable.stat_sys_upload, title, "$progress%",
                    applicationContext, 100, progress, true)
            } else if (count > 1) {
                notifyProgress(
                    notification_id,
                    R.drawable.stat_sys_upload,
                    "Viola",
                    "Uploading $count ",
                    applicationContext,
                    100,
                    0,
                    true
                )
            }
        }

    }


    private fun uploadPost(
        notification_id: Int,
        currentUser_id: String?,
        description: String?,
        uploadedImagesUrl: ArrayList<String>?,
        postID: String,
        title:String,originalPrice:String,discountPrice:String,city: String,minPeople: String) {
        if (uploadedImagesUrl!!.isNotEmpty()) {
            if (count == 1) {
                notifyProgress(
                    notification_id,
                    R.drawable.stat_sys_upload,
                    "Voila",
                    "Uploading Offer..",
                    applicationContext,
                    100,
                    0,
                    true
                )
            }
            /*else if(count>1)
            {
                notifyProgress(notification_id+1
                        , android.R.drawable.stat_sys_upload
                        , "Hify"
                        , "Sending post.."
                        , getApplicationContext()
                        , 100
                        , 0
                        , true);
            }
            */
            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUser_id!!)
                .get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                    val postMap: MutableMap<String, Any?> = HashMap()
                    val userPostMap: MutableMap<String, Any?> = HashMap()
                    val id =documentSnapshot.getString("userId")

                    postMap["userId"] = documentSnapshot.getString("userId")
                    postMap["username"] = documentSnapshot.getString("username")
                    postMap["name"] = documentSnapshot.getString("username")
                    postMap["thumbnailImage"] = documentSnapshot.getString("thumbnailImage")
                    postMap["timestamp"] = FieldValue.serverTimestamp()
                    postMap["image_count"] = uploadedImagesUrl.size
                    try {
                        postMap["image_url_0"] = uploadedImagesUrl[0]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        postMap["image_url_1"] = uploadedImagesUrl[1]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        postMap["image_url_2"] = uploadedImagesUrl[2]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        postMap["image_url_3"] = uploadedImagesUrl[3]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        postMap["image_url_4"] = uploadedImagesUrl[4]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        postMap["image_url_5"] = uploadedImagesUrl[5]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        postMap["image_url_6"] = uploadedImagesUrl[6]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    postMap["description"] = description
                    postMap["postId"]= postID
                    postMap["title"]=title
                    postMap["originalPrice"]=originalPrice
                    postMap["discountPrice"]=discountPrice
                    postMap["city"]=city
                    postMap["ratings"] = "0"
                    postMap["usersRated"] = "0"
                    postMap["addressMap"] = "none"
                    postMap["offerCategory"] = ""
                    postMap["minPeople"] = minPeople
                    postMap["maxPeople"] = ""

                    userPostMap[postID]=true

                    FirebaseFirestore.getInstance().collection("Offers").document(postID)
                        .set(postMap, SetOptions.merge()).addOnSuccessListener {

                            FirebaseFirestore.getInstance().collection("Offers")
                                .document(postID).get().addOnSuccessListener {

                                    val offerRoomEntity: OfferRoomEntity? = it?.toObject(OfferRoomEntity::class.java)

                                    val recentMap: MutableMap<String, Any?> = HashMap()

                                    recentMap["userId"] = documentSnapshot.getString("userId")
                                    recentMap["username"] = documentSnapshot.getString("username")
                                    recentMap["name"] = documentSnapshot.getString("username")
                                    recentMap["thumbnailImage"] = documentSnapshot.getString("thumbnailImage")
                                    if (offerRoomEntity != null) {
                                        recentMap["timestamp"] = offerRoomEntity.timestamp
                                    }
                                    recentMap["image_count"] = uploadedImagesUrl.size
                                    recentMap["description"] = description
                                    recentMap["postId"]= postID
                                    recentMap["title"]=title
                                    recentMap["originalPrice"]=originalPrice
                                    recentMap["discountPrice"]=discountPrice
                                    recentMap["city"]=city
                                    recentMap["ratings"] = "0"
                                    recentMap["usersRated"] = "0"
                                    recentMap["addressMap"] = "none"
                                    recentMap["offerCategory"] = ""
                                    recentMap["minPeople"] = minPeople
                                    recentMap["maxPeople"] = ""
                                    try {
                                        recentMap["image_url_0"] = uploadedImagesUrl[0]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        recentMap["image_url_1"] = uploadedImagesUrl[1]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        recentMap["image_url_2"] = uploadedImagesUrl[2]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        recentMap["image_url_3"] = uploadedImagesUrl[3]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        recentMap["image_url_4"] = uploadedImagesUrl[4]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        recentMap["image_url_5"] = uploadedImagesUrl[5]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        recentMap["image_url_6"] = uploadedImagesUrl[6]
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    FirebaseFirestore.getInstance().collection("Offers").document("recentOffer")
                                        .set(recentMap).addOnSuccessListener {
                                            Log.d("MyTag","OfferAdded")
                                        }.addOnFailureListener {
                                            Log.d("MyTag","Offer Not Added "+it.localizedMessage)
                                        }


                                }

                        }
//                        .continueWith {
//                            if (id != null) {
//                                FirebaseFirestore.getInstance().collection("Users").document(id)
//                                    .collection("Offers").document("UploadedOffers")
//                                    .update("UploadedOffers", FieldValue.arrayUnion(postID))
//                            }
//                        }
                        .addOnSuccessListener {
                            getSharedPreferences("uploadService2", MODE_PRIVATE).edit().putInt("count", --count).apply()
                            Toasty.success(applicationContext, "Offer added", Toasty.LENGTH_SHORT, true).show()
                            Log.d(myTag, "count is $count")
                            if (count == 0) {
                                stopForegroundService(true)
                            }
                        }
                        .addOnFailureListener { e: Exception ->
                            Toasty.error(applicationContext, "Error :" + e.message, Toasty.LENGTH_SHORT, true).show()
                            stopForegroundService(true)
                            e.printStackTrace()
                        }
                }.addOnFailureListener { e: Exception ->
                    Toasty.error(applicationContext, "Error :" + e.message, Toasty.LENGTH_SHORT, true).show()
                    stopForegroundService(true)
                    e.printStackTrace()
                }
        } else {
            Toasty.info(this, "No image has been uploaded, Please wait or try again", Toasty.LENGTH_SHORT, true).show()
            stopForegroundService(true)
        }
    }

}