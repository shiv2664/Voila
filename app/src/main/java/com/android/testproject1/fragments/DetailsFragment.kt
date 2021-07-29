package com.android.testproject1.fragments

import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.adapter.PagerPhotosAdapter
import com.android.testproject1.adapter.DetailsPhotosAdapter
import com.android.testproject1.adapter.PostsAdapter2
import com.android.testproject1.databinding.FragmentDetailsBinding
import com.android.testproject1.model.MultipleImage
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.viewmodels.DetailsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.ArrayList


class DetailsFragment : Fragment() {

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mViewModel: DetailsFragmentViewModel
    private lateinit var binding: FragmentDetailsBinding
    private var myTag:String="MyTag"
    private var currentUserId: String?=null
    private var name: String?=null
    private lateinit var postItem : Post



    companion object{
        var postId:String?=null
    }
//    var staticTest get() = test  // getter for test


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
//        context?.theme?.applyStyle(R.style.AppTheme, true);
        binding= FragmentDetailsBinding.inflate(inflater, container, false)

        val bundle = this.arguments
         postItem= bundle?.getParcelable<Post>("PostItem")!!
        setUrls(binding,postItem)


        postId=postItem.postId

        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()
        currentUserId = firebaseAuth.currentUser?.uid

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))
            .get(DetailsFragmentViewModel::class.java)


        mViewModel.loadGroupsOnPost(postItem.postId)

//        var fragment=GroupFragment()
//
//        var bundle2= Bundle()
//        bundle.putString("postId",postItem.postId)
//        fragment.arguments=bundle2

        binding.descriptionText.text = postItem.description


        currentUserId?.let {
            firebaseFirestore.collection("Users").document(it).get().addOnSuccessListener {
                if (it!=null){
                    val notifPojo: Users? = it.toObject(Users::class.java)
                    if (notifPojo != null) {
                        name=notifPojo.name
                    }
                }
            }
        }


        mViewModel.getUserList().observe(viewLifecycleOwner, {
            binding.userList=it

        })


//        if (postItem != null) {
//            binding.DescriptionText.text = postItem.description
//            activity?.let { Glide.with(it).load(postItem.image_url_0).into(binding.roundedImageView) }
//        }

        binding.extendedFab.setOnClickListener {
//            val postUserId: String? = postItem?.userId
            val postId: String? = postItem?.postId

            val postMap: MutableMap<String, Any?> = HashMap()
            val postIdMap: MutableMap<String, Any?> = HashMap()

            postIdMap["postId"]=postId

            if (currentUserId!=null) {

                postMap["createdBy"] = currentUserId

                if (postId != null) {
                    Log.d(myTag, "$postId   $currentUserId")


                    firebaseFirestore.collection("Posts").document(postId)
                        .collection("Groups").document(currentUserId!!).set(postMap, SetOptions.merge()).addOnSuccessListener {

                            firebaseFirestore.collection("Posts").document(postId)
                                .collection("Groups").document(currentUserId!!)
                                .update("Members",FieldValue.arrayUnion("$currentUserId"))

                        }

                }
            }

        }

        return binding.root
    }

    private fun setUrls(binding: FragmentDetailsBinding,post: Post) {

        val imagesList1: java.util.ArrayList<MultipleImage> = ArrayList()
        val indicator = binding.indicator
        val indicatorHolder = binding.indicatorHolder
        indicator.dotsClickable = true

        val adapter = DetailsPhotosAdapter(activity, imagesList1,false)

//        val photosAdapter= DetailsPhotosAdapter(context,imagesList1,false)

//        , multipleImages: ArrayList<MultipleImage>, photosAdapter: PostPhotosAdapter

//        val pos: Int = holder.adapterPosition

        val url0: String = post.image_url_0
        val url1: String = post.image_url_1
        val url2: String = post.image_url_2
        val url3: String = post.image_url_3
        val url4: String = post.image_url_4
        val url5: String = post.image_url_5
        val url6: String = post.image_url_6

        if (!TextUtils.isEmpty(url0)) {
            val image = MultipleImage(url0)
            imagesList1.add(image)
            adapter.notifyDataSetChanged()
            Log.d("MyTag", url0)
        }
        if (!TextUtils.isEmpty(url1)) {
            val image = MultipleImage(url1)
            imagesList1.add(image)
            adapter.notifyDataSetChanged()
            Log.d("url1", url1)
        }
        if (!TextUtils.isEmpty(url2)) {
            val image = MultipleImage(url2)
            imagesList1.add(image)
            adapter.notifyDataSetChanged()
            Log.d("url2", url2)
        }
        if (!TextUtils.isEmpty(url3)) {
            val image = MultipleImage(url3)
            imagesList1.add(image)
            adapter.notifyDataSetChanged()
            Log.d("url3", url3)
        }
        if (!TextUtils.isEmpty(url4)) {
            val image = MultipleImage(url4)
            imagesList1.add(image)
            adapter.notifyDataSetChanged()
            Log.d("url4", url4)
        }
        if (!TextUtils.isEmpty(url5)) {
            val image = MultipleImage(url5)
            imagesList1.add(image)
            adapter.notifyDataSetChanged()
            Log.d("url5", url5)
        }
        if (!TextUtils.isEmpty(url6)) {
            val image = MultipleImage(url6)
            imagesList1.add(image)
            adapter.notifyDataSetChanged()
            Log.d("ur6", url6)
        }

        binding.pager.adapter = adapter

        if (imagesList1.size > 1) {
            indicatorHolder.visibility = View.VISIBLE
            indicator.setViewPager(binding.pager)
        } else {
            indicatorHolder.visibility = View.GONE
        }


        Log.d("MyTag", "Images are ${imagesList1.size}")
    }

}