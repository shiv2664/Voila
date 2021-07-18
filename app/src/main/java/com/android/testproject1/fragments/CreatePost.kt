package com.android.testproject1.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.android.testproject1.R
import com.android.testproject1.adapter.PagerPhotosAdapter
import com.android.testproject1.databinding.FragmentCreatePostBinding
import com.android.testproject1.services.UploadService
import com.android.testproject1.viewmodels.CreatePostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class CreatePost : Fragment() {

    private lateinit var adapter: PagerPhotosAdapter
    private lateinit var mViewModel: CreatePostViewModel

    private var imagesList1: ArrayList<Image> = java.util.ArrayList()
    private var uploadedImagesUrl = ArrayList<String>()

    var myTag: String = "MyTag"

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var postID: String? = null

    var currentId: String? = null
    private val pICKIMAGES = 102

    private lateinit var sharedPreferences: SharedPreferences
    private var serviceCount = 0

    private lateinit var binding: FragmentCreatePostBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(CreatePostViewModel::class.java)

        sharedPreferences =
            requireActivity().getSharedPreferences("uploadservice", Context.MODE_PRIVATE)
        serviceCount = sharedPreferences.getInt("count", 0)

//        val toolbar = binding.createPostToolbar
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.setTitle(com.android.testproject1.R.string.Explore)
//        setHasOptionsMenu(true)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentId = firebaseAuth.currentUser?.uid
        Log.d(myTag, "image list size is" + imagesList1.size.toString())

        postID = currentId + System.currentTimeMillis().toString()

        startPickImage()

//        if (imagesList1.size==0){
//            binding.Upload.visibility=View.VISIBLE
//        }else {
//            binding.Upload.visibility=View.INVISIBLE
//        }
//
//        binding.Upload.setOnClickListener {
//            if (imagesList1.size== 0){
//                startPickImage()
//            }
//        }


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
            .setLimitMessage("You can select up to 6 images")
            .setSelectedImages(imagesList1)
            .setRequestCode(pICKIMAGES)
            .start();
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.Post) {


            if (binding.descriptionTextMain.text.isEmpty()) {
//                AnimationUtil.shakeView(mEditText, activity)

            } else {

                sharedPreferences.edit().putInt("count", ++serviceCount).apply()
                Log.d(myTag, "On click  sp $serviceCount")

                val intent = Intent(activity, UploadService::class.java)

                intent.putExtra("count", serviceCount)

                intent.putStringArrayListExtra("uploadedImagesUrl", uploadedImagesUrl)

                intent.putParcelableArrayListExtra(
                    "imagesList",
                    imagesList1 as java.util.ArrayList<out Parcelable?>?
                )

                intent.putExtra("notification_id", System.currentTimeMillis().toInt())

                intent.putExtra("current_id", currentId)

                intent.putExtra("postID", postID)

                intent.putExtra("description", binding.descriptionTextMain.text.toString().trim())

                intent.action = UploadService.ACTION_START_FOREGROUND_SERVICE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    requireActivity().startForegroundService(intent)

                    Log.d(myTag, "Build Version OP")
//                startForegroundService(activity!!,intent)
                } else {

                    Log.d(myTag, "Build Version NP")
//                activity!!.startService(intent)
                    requireActivity().startService(intent)

                }
                Toasty.info(requireActivity(), "Uploading images..", Toasty.LENGTH_SHORT, true)
                    .show()
//            activity!!.finish()

                val fragment: Fragment = HomeFragment()
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()


            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, pICKIMAGES)) {
            imagesList1 = ImagePicker.getImages(data)


//            Log.d(myTag, imagesList1.toString())


            activity?.let {
                MaterialDialog.Builder(it)
                    .title("Confirmation")
                    .content("Are you sure do you want to continue?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .neutralText("Cancel")
                    .onPositive { _, _ ->


                        val indicator = binding.indicator
                        val indicatorHolder = binding.indicatorHolder
                        indicator.dotsClickable = true
                        adapter = PagerPhotosAdapter(activity, imagesList1)
                        binding.pager.adapter = adapter

                        if (imagesList1.size > 1) {
                            indicatorHolder.visibility = View.VISIBLE
                            indicator.setViewPager(binding.pager)
                        } else {
                            indicatorHolder.visibility = View.GONE
                        }


                        val fragment = CreatePost()
                        val b = Bundle()
                        b.putParcelableArrayList("imagesList", imagesList1)
                        Log.d(myTag, "picked images are--- $imagesList1")


                        fragment.arguments = b
                        requireActivity()
                            .supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit()

                    }


                    .onNegative { _, _ ->

                        ImagePicker.with(this)
                            .setFolderMode(true)
                            .setFolderTitle("Album")
                            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                            .setDirectoryName("Image Picker")
                            .setMultipleMode(true)
                            .setShowNumberIndicator(true)
                            .setMaxSize(10)
                            .setLimitMessage("You can select up to 6 images")
//                .setSelectedImages(imagesList1)
                            .setRequestCode(pICKIMAGES)
                            .start();

                    }.show()

            }

            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}








