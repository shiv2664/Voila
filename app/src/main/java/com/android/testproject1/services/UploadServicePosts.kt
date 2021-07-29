package com.android.testproject1.services

import android.R
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.nguyenhoanglam.imagepicker.model.Image
import es.dmoral.toasty.Toasty
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class UploadServicePosts : Service() {


    companion object{
        val ACTION_START_FOREGROUND_SERVICE_UPLOAD_POST = "ACTION_START_FOREGROUND_SERVICE"
    }

    var numberImages:Int=0
    private var count = 0
    private var bitmap: Bitmap? = null
    private var resized: Bitmap? = null
    val myTag:String = "MyTag"



    override fun onCreate() {

        Log.d(myTag, "Service Created ")

        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(myTag, "$intent   $flags  $startId")

        if (intent!=null) {
            val action = intent.action
            if (action == ACTION_START_FOREGROUND_SERVICE_UPLOAD_POST) {

                val imagesList: ArrayList<Image>? =
                    intent.getParcelableArrayListExtra<Image>("imagesList")

                val notification_id = intent.getIntExtra("notification_id", 2)

                val postID = intent.getStringExtra("postID")

                val current_id = intent.getStringExtra("current_id")

                val description = intent.getStringExtra("description")

                val uploadedImagesUrl = intent.getStringArrayListExtra("uploadedImagesUrl")
                count = intent.getIntExtra("count", 0)

                if (imagesList != null) {
                    if (postID != null) {
                        uploadImages(notification_id, 0, imagesList, current_id, description, uploadedImagesUrl, postID)
                    }
                }
            }

        }


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
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

//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Example Service")
//            .setContentText(input)
//            .setSmallIcon(R.drawable.ic_android)
//            .setContentIntent(pendingIntent)
//            .build()
//        startForeground(1, notification)


        val builder = NotificationCompat.Builder(context, App.CHANNEL_ID)
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
            .setChannelId(App.CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(max_progress, progress, indeterminate)
            .setVibrate(LongArray(0))
        startForeground(id, builder.build())

    }


    fun getImageUri(inContext: Context, inImage: Bitmap) {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var path: String?=null
        try {
             path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)

        }catch (e: java.lang.Exception){
            e.localizedMessage?.toString()?.let { Log.d(myTag, it) }
        }
        if (path!=null){
            Log.d(myTag, Uri.parse(path).toString())
        }
        else{
            Log.d(myTag, "Path is null ")
        }


    }


    private fun uploadImages(
        notification_id: Int,
        index: Int,
        imagesList: ArrayList<Image>,
        currentUser_id: String?,
        description: String?,
        uploadedImagesUrl: ArrayList<String>?,
        postID:String
    ) {
        val img_count = index + 1

        var imageUri: Uri

        val imageUri0: Uri?= Uri.fromFile(File(imagesList[index].path))

        if (Build.VERSION.SDK_INT >= 29) {
            try {
                bitmap = imageUri0?.let {ImageDecoder.createSource(this.contentResolver,it)}?.let { ImageDecoder.decodeBitmap(it) }
            } catch (e: IOException) {
                e.printStackTrace()

                e.localizedMessage?.toString()?.let { Log.d(myTag, " errore is  $it") }
            }
        } else {
            // Use older version
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri0)
            } catch (e: IOException) {
                e.printStackTrace()
                e.localizedMessage?.toString()?.let { Log.d(myTag, " errore is  $it") }
            }
        }

//        val bitmap = BitmapFactory.decodeFile(file.getAbsolutePath())

        resized = bitmap?.let { Bitmap.createScaledBitmap(it, 600, 600, true) }
//        Log.d(myTag, "path is  ${bitmap.toString()}")

        var path :String?=null
        try {
//            path = MediaStore.Images.Media.insertImage(this.contentResolver, resized, "Title", null)
            path = MediaStore.Images.Media.insertImage(this.contentResolver, resized,  "IMG_"
                    + System.currentTimeMillis(), null)
            Log.d(myTag, "path is  $path")
        }catch (e :java.lang.Exception){
            Log.d(myTag, "path is exception $path" )
            Log.d(myTag, e.localizedMessage.toString() )

        }


            imageUri = Uri.parse(path)


//        imageUri = try {
//
//            val compressedFile: File = id.zelory.compressor.Compressor()
//                .setQuality(80)
//                .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                .compressToFile(File(imagesList[index].path))
//            Uri.fromFile(compressedFile)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Uri.fromFile(File(imagesList!![index].path))
//        }


        val fileToUpload = currentUser_id?.let {
            FirebaseStorage.getInstance().reference.child("Posts").child(it)
                .child(postID)
                .child("Viola_" + System.currentTimeMillis() + "_" +numberImages)
        }
        numberImages++
        fileToUpload?.putFile(imageUri)
            ?.addOnSuccessListener {
                Log.d(myTag, "Uploaded Successfully")
                fileToUpload.downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        uploadedImagesUrl!!.add(uri.toString())
                        val next_index = index + 1
                        try {
                            if (!TextUtils.isEmpty(imagesList[index + 1].path)) {
                                uploadImages(
                                    notification_id,
                                    next_index,
                                    imagesList,
                                    currentUser_id,
                                    description,
                                    uploadedImagesUrl,
                                    postID
                                )
                            } else {
                                uploadPost(
                                    notification_id,
                                    currentUser_id,
                                    description,
                                    uploadedImagesUrl,
                                    postID
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            uploadPost(
                                notification_id,
                                currentUser_id,
                                description,
                                uploadedImagesUrl, postID
                            )
                        }
                    }
                    .addOnFailureListener { obj: Exception -> obj.printStackTrace() }
            }
            ?.addOnFailureListener { obj: Exception ->
                obj.printStackTrace()
                obj.localizedMessage?.toString()?.let { Log.d(myTag, "Exception is  $it") }
            }
            ?.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                if (count == 1) {
                    val title = "Uploading " + img_count + "/" + imagesList.size + " images..."
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    notifyProgress(
                        notification_id,
                        R.drawable.stat_sys_upload,
                        title,
                        "$progress%",
                        applicationContext,
                        100,
                        progress,
                        false
                    )
                } else if (count > 1) {
                    notifyProgress(
                        notification_id,
                        R.drawable.stat_sys_upload,
                        "Viola",
                        "Uploading $count posts",
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
        postID: String
    ) {
        if (uploadedImagesUrl!!.isNotEmpty()) {
            if (count == 1) {
                notifyProgress(
                    notification_id,
                    R.drawable.stat_sys_upload,
                    "Viola",
                    "Uploading Post..",
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
                    val id =documentSnapshot.getString("id")

                    postMap["userId"] = documentSnapshot.getString("id")
                    postMap["username"] = documentSnapshot.getString("username")
                    postMap["name"] = documentSnapshot.getString("name")
//                    postMap["userimage"] = documentSnapshot.getString("imgUrl")
                    postMap["timestamp"] = System.currentTimeMillis().toString()
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
                    postMap["color"] = "0"

                    userPostMap[postID]=true

                    FirebaseFirestore.getInstance().collection("Posts").document(postID)
                        .set(postMap,SetOptions.merge()).continueWith {
                            if (id != null) {
                                FirebaseFirestore.getInstance().collection("Users").document(id)
                                    .collection("Posts").document("UploadedPosts")
                                    .update("UploadedPosts", FieldValue.arrayUnion(postID))
                            }
                        }
                        .addOnSuccessListener {
                            getSharedPreferences("uploadservice", MODE_PRIVATE).edit().putInt("count", --count).apply()
                            Toasty.success(applicationContext, "Post added", Toasty.LENGTH_SHORT, true).show()
                            Log.d(myTag, "count is $count")
                            if (count == 0) {
                                stopForegroundService(true)
                            }
                        }
                        .addOnFailureListener { e: Exception ->
                            Toasty.error(
                                applicationContext,
                                "Error :" + e.message,
                                Toasty.LENGTH_SHORT,
                                true
                            )
                                .show()
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