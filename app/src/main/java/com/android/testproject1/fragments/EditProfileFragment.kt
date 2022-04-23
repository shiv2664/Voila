package com.android.testproject1.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentEditProfileBinding
import com.android.testproject1.model.Users
import com.android.testproject1.services.UploadServiceOffers
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private val pickProfileCode = 102
    private val pickProfileLib = 110
    private lateinit var storageReference: StorageReference
    private val Storage_Permission_Req = 1
    private var imagesList1: ArrayList<Image> = ArrayList()
    var resultUri: Uri? = null
    var bitmap: Bitmap? = null
    var resized: Bitmap? = null
    private var uploadTask: StorageTask<*>? = null
    var imageUri: Uri? = null
    var map = HashMap<String, Any>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()

        binding.setProfilePic.setOnClickListener {

            if (activity?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } == PackageManager.PERMISSION_GRANTED) {
                openFileChooser()
//                startPickImage2()
            } else {
                requestStoragePermission()
            }
        }

        val reference = FirebaseFirestore.getInstance()
        val fuser = FirebaseAuth.getInstance().currentUser?.uid
        reference.collection("Users").document(fuser.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                // convert document to POJO
                val notifPojo: Users? = it.toObject(Users::class.java)

                if (notifPojo != null) {
                    if (notifPojo.profileimage.isNotEmpty()) {
                        activity?.let { it1 ->
                            Glide.with(it1)
                                .load(notifPojo.profileimage)
                                .addListener(imageLoadingListener(binding.lottiViewProfile))
                                .into(binding.profileImage)
                        }
//                            binding.name.setText( notifPojo.name)
//                            map["name"]=notifPojo.name
//                            map["name"]=notifPojo.username
//                            map["bio"]=notifPojo.bio
                    }
                    binding.name.setText(notifPojo.username)
                    binding.Bio.setText(notifPojo.bio)
                    Log.d("MyTag", " Image Url is " + notifPojo.profileimage)
                }
            }

        }.addOnFailureListener {
            Log.d("MyTag", " Exception is : "+it.localizedMessage)
        }

        binding.saveButton.setOnClickListener {

            if (resultUri != null) {
                val intent = Intent(requireActivity(), UploadServiceOffers::class.java)
                intent.putExtra("imageUri", resultUri.toString())
                intent.action =
                    UploadServiceOffers.ACTION_START_FOREGROUND_SERVICE_UPLOAD_PROFILE_PIC

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireActivity().startForegroundService(intent)
                } else {
                    requireActivity().startService(intent)
                }
            }

            val username=binding.name.text.toString().trim()
            val bio=binding.Bio.text.toString().trim()
            map["username"]=username
            map["bio"]=bio
            firebaseAuth.currentUser?.let { it1 -> firestore.collection("Users").document(it1.uid)
                .set(map, SetOptions.merge()) }

        }

        binding.changePassword.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_changePasswordFragment)
        }

        return binding.root
    }

    private fun startPickImage2() {
        imagesList1.clear()
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle("Album")
            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
            .setDirectoryName("Image Picker")
            .setMultipleMode(true)
            .setShowNumberIndicator(true)
            .setMaxSize(1)
//            .setLimitMessage("You can select up to 6 images")
            .setSelectedImages(imagesList1)
            .setRequestCode(pickProfileLib)
            .start();
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, pickProfileCode)
    }

    private fun requestStoragePermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf<String?>(Manifest.permission.READ_EXTERNAL_STORAGE),
                Storage_Permission_Req
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Storage_Permission_Req) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
                openFileChooser()
//                startPickImage2()
            } else {
                Toast.makeText(activity, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == pickProfileCode && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
//            addPic()

            context?.let {
                CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(it, this)
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.uri
                Log.d("MyTag", "resultUri is in ActivityResult : $resultUri")
                addPic()

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("MyTag", "error is: " + result.error)
            }
        }


        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun imageLoadingListener(pendingImage: LottieAnimationView): RequestListener<Drawable?>? {
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

    private fun addPic() {
        Log.d("MyTag", "resultUri is in fnc : $imageUri")

        if (resultUri != null) {

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap = ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(activity!!.contentResolver, resultUri!!)
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                // Use older version
                try {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(activity!!.contentResolver, resultUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

//            resized = Bitmap.createScaledBitmap(bitmap!!, 100, 100, true)
            resized = bitmap

            binding.profileImage.setImageBitmap(resized)
//            Glide.with(requireActivity())
////                    .setDefaultRequestOptions(requestOptions)
//                .asBitmap()
//                .load(resized)
//                .into(binding.profileImage)

        } else {
            binding.profileImage.setImageResource(R.drawable.ic_person_black_24dp);
        }

    }

    fun updatePassword(){
        val user = Firebase.auth.currentUser!!

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider.getCredential("user@example.com", "password1234")

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnSuccessListener {

            }
    }
}


