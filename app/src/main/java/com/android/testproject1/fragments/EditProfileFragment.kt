package com.android.testproject1.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.testproject1.databinding.FragmentEditProfileBinding
import com.android.testproject1.model.Users
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private val pickProfileCode = 102
    private lateinit var storageReference: StorageReference
    private val Storage_Permission_Req = 1
    private var imagesList1: ArrayList<Image> = java.util.ArrayList()
    lateinit var resultUri: Uri
    var bitmap: Bitmap? = null
    var resized: Bitmap? = null
    private var uploadTask: StorageTask<*>? = null
    var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        storageReference = FirebaseStorage.getInstance().reference

        binding.setProfilePic.setOnClickListener {

            if (activity?.let { it1 ->
                    ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE)
                } == PackageManager.PERMISSION_GRANTED) {
                openFileChooser()
            } else {
                requestStoragePermission()
            }

        }

        val reference=FirebaseFirestore.getInstance()
        val fUser=FirebaseAuth.getInstance().currentUser?.uid
        reference.collection("Users").document(fUser.toString()).get().addOnSuccessListener {
            if (it != null) {

                if (it.exists()) {
                    // convert document to POJO
                    val notifPojo: Users? = it.toObject(Users::class.java)

                    if (notifPojo != null) {
                        if (notifPojo.imgUrl.isNotEmpty()) {
                            activity?.let { it1 -> Glide.with(it1).load(notifPojo.imgUrl).into(binding.profileImage) }
                        }
                            Log.d("MyTag"," Image Url is "+notifPojo.imgUrl)
                        }
                    }
                }

            }





//        var reference = fuser.getUid()?.let { FirebaseDatabase.getInstance().getReference("Users").child(it) }
//        reference!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val user = dataSnapshot.getValue(User::class.java)
//                if (user?.imageURL == "default") {
//                    image_profile.setImageResource(R.drawable.user2)
//                } else {
//                    if (user != null) {
//                        Glide.with(applicationContext).load(user.imageURL).into(image_profile)
//                    }
//                }
//            }







        return binding.root
    }

    private fun startPickImage() {
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle("Album")
            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
            .setDirectoryName("Image Picker")
            .setMultipleMode(true)
            .setShowNumberIndicator(true)
            .setMaxSize(6)
            .setLimitMessage("You can select up to 10 images")
            .setSelectedImages(imagesList1)
            .setRequestCode(pickProfileCode)
            .start();
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,pickProfileCode)
    }

    private fun requestStoragePermission() {
        activity?.let { ActivityCompat.requestPermissions(it, arrayOf<String?>(Manifest.permission.READ_EXTERNAL_STORAGE), Storage_Permission_Req) }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Storage_Permission_Req) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
                openFileChooser()
            } else {
                Toast.makeText(activity, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == pickProfileCode && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.data
            resultUri = imageUri!!

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap =
                        activity?.let { ImageDecoder.createSource(it.contentResolver, resultUri) }?.let {
                            ImageDecoder.decodeBitmap(
                                it
                            )
                        }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                // Use older version
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, resultUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            uploadingProfileImage()
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun uploadingProfileImage() {
//        val f = File(resultUri.getPath())
//        val sizeUri = f.length() / 1024

        /*
        int quality;
        if (sizeUri<=25)
            quality = 70;
        else if (sizeUri <= 300)
            quality = 30;
        else if (sizeUri <= 1000)
            quality = 10;
        else if (sizeUri <= 2000)
            quality = 10;
        else if (sizeUri <= 3000)
            quality = 10;
        else
            quality= 5;

         */


        resized = bitmap?.let { Bitmap.createScaledBitmap(it, 50, 50, true) }
        val baos = ByteArrayOutputStream()
        resized?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val uploadbaos = baos.toByteArray()

        val fUser = FirebaseAuth.getInstance().currentUser?.uid
         val reference = FirebaseFirestore.getInstance()
        fUser?.let { reference.collection("Users").document(it) }


        val fileReference = storageReference.child("$fUser.jpg")

        uploadTask = fileReference.putBytes(uploadbaos)

        (uploadTask as UploadTask).continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            } else if (task.isSuccessful) {
                //Toast.makeText(EditCredActivity.this, "Upadated Successfully", Toast.LENGTH_SHORT).show();
            }
            fileReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val mUri = downloadUri.toString()
                if (fUser != null) {
                   var imgRef= reference.collection("Users").document(fUser)
                    val map = HashMap<String?, Any?>()
                    map["imgUrl"] = mUri
                    imgRef.update(map)
                }


                //pd.dismiss();
            } else {
                Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                //pd.dismiss();
            }
        }.addOnFailureListener { e ->
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            //pd.dismiss();
        }
    }

}