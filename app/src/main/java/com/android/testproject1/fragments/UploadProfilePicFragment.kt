package com.android.testproject1.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.MainActivity2
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentUploadProfilePicBinding
import com.android.testproject1.services.UploadServiceOffers
import com.android.testproject1.viewmodels.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import java.io.IOException

class UploadProfilePicFragment : Fragment() {

    private lateinit var mViewModel: SignUpViewModel
    private lateinit var binding:FragmentUploadProfilePicBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val PICK_IMAGE_REQUEST = 1
    private val Storage_Permission_Req = 1
    private var imageUri: Uri? = null
    var resultUri: Uri?=null
    var bitmap: Bitmap? = null
    var resized: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUploadProfilePicBinding.inflate(inflater,container,false)

        firebaseAuth= FirebaseAuth.getInstance()
//        val username = arguments!!.getString("fullName")
//        val email=arguments!!.getString("email")
//        val password1=arguments!!.getString("password1")
//        val phoneNumber=arguments!!.getString("phoneNumber")

//        Log.d("MyTag","username is $username")

        mViewModel= ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))
            .get(SignUpViewModel::class.java)

//        mViewModel.getUserLiveData().observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                val intent = Intent(activity, MainActivity2::class.java)
//                startActivity(intent)
//            }
//        })

        addPic()

        binding.profileImage.setOnClickListener {
                if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    openFileChooser()
                } else {
                    requestStoragePermission()
                }

        }

        binding.signUp.setOnClickListener {
            if (resultUri!=null){
                val intent = Intent(requireActivity(), UploadServiceOffers::class.java)
                intent.putExtra("imageUri",resultUri.toString())
                intent.action = UploadServiceOffers.ACTION_START_FOREGROUND_SERVICE_UPLOAD_PROFILE_PIC

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireActivity().startForegroundService(intent)
                } else {
                    requireActivity().startService(intent)
                }
                val intent2 = Intent(activity, MainActivity2::class.java)
                startActivity(intent2)
                activity?.finish()
            }else{
                Toasty.error(requireActivity(),"Please Select a Profile Picture",Toasty.LENGTH_SHORT).show()
            }

//            if (resultUri!=null){
//                if (email != null && password1!=null && username!=null&&phoneNumber!=null) {
//                    mViewModel.register(email,password1,username,phoneNumber)
//                    val intent = Intent(requireActivity(), UploadServiceOffers::class.java)
//                    intent.putExtra("imageUri",imageUri.toString())
//                    intent.action = UploadServiceOffers.ACTION_START_FOREGROUND_SERVICE_UPLOAD_PROFILE_PIC
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        requireActivity().startForegroundService(intent)
//                    } else {
//                        requireActivity().startService(intent)
//                    }
////                    Toasty.info(requireActivity(), "Updating Profile Picture...", Toasty.LENGTH_SHORT, true).show()
////                    val intent2=Intent(requireActivity(),MainActivity2::class.java)
////                    startActivity(intent2)
//
//
//                }
//            }else{
//                if (email != null && password1!=null && username!=null&&phoneNumber!=null) {
//                    mViewModel.register(email,password1,username,phoneNumber)
////                    val intent2=Intent(requireActivity(),MainActivity2::class.java)
////                    startActivity(intent2)
//                }
//            }
        }

        return binding.root
    }


    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            , Storage_Permission_Req)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Storage_Permission_Req) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT)
                    .show()
                openFileChooser()
            } else {
                Toast.makeText(requireActivity(), "Permission Not Granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data

            context?.let {
                CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(it,this)
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.uri
                Log.d("MyTag", "resultUri is in ActivityResult : $resultUri")
                addPic()

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("MyTag","error is: "+result.error)
            }
        }


        Log.d("MyTag", "request Code : $requestCode PICK_IMAGE_REQUEST : $PICK_IMAGE_REQUEST CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ${CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE} Activity.RESULT_OK ${Activity.RESULT_OK}")




    }

    private fun addPic(){
        Log.d("MyTag","resultUri is in fnc : "+imageUri)

        if (resultUri!=null){

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity!!.contentResolver, resultUri!!)
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                // Use older version
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver,resultUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            resized = Bitmap.createScaledBitmap(bitmap!!, 100, 100, true)

            binding.profileImage.setImageBitmap(resized)
//            Glide.with(requireActivity())
////                    .setDefaultRequestOptions(requestOptions)
//                .asBitmap()
//                .load(resized)
//                .into(binding.profileImage)

        }else{
            binding.profileImage.setImageResource(R.drawable.ic_person_black_24dp);
        }

    }




}