package com.android.testproject1.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.testproject1.databinding.FragmentChangePasswordBinding
import com.android.testproject1.databinding.FragmentSingleOfferBinding
import com.android.testproject1.databindingadapters.bind
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.bumptech.glide.Glide

import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import com.afollestad.materialdialogs.MaterialDialog
import com.android.testproject1.CreateOffer
import com.android.testproject1.room.enteties.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch


class SingleOfferFragment : Fragment() {
    private lateinit var binding: FragmentSingleOfferBinding

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage:FirebaseStorage

    private lateinit var offerItem:OfferRoomEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSingleOfferBinding.inflate(inflater,container,false)

        firebaseStorage= FirebaseStorage.getInstance()
        firestore= FirebaseFirestore.getInstance()

        offerItem= arguments?.getParcelable("offerItem")!!
        updateFields()

        Log.d("MyTag","Offer Item entries are : "+offerItem?.title)

//        if (offerItem != null) {
//            Glide.with(requireActivity())
//                .load(offerItem.image_url_0)
//                .into(binding.img)
//            Glide.with(requireActivity())
//                .load(offerItem.thumbnailImage)
//                .into(binding.profileImage)
//        }
//        binding.DiscountedPrice.text="₹"+offerItem?.discountPrice
//        binding.DiscountedPriceEditText.setText("₹"+offerItem?.discountPrice)
//
//        binding.username.text=offerItem?.username
//
//        binding.offer.text=offerItem?.title
//        binding.descriptionTextTitle.setText(offerItem?.title)
//
//        binding.desc.text=offerItem?.description
//        binding.descriptionTextMain.setText(offerItem?.description)
//
//        binding.OriginalPrice.setText(offerItem?.originalPrice)

        binding.DeleteOffer.setOnClickListener {

            MaterialDialog.Builder(requireActivity())
                .title("Confirmation")
                .content("Are you sure do you want to delete?")
                .positiveText("Yes")
                .negativeText("No")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .neutralText("Cancel")
                .onPositive { _, _ ->

                    val storageRef = firebaseStorage.reference
                    val desertRef = storageRef.child("Offers").child(offerItem.userId).child(offerItem.postId)
                    desertRef.delete().continueWith {
                        firestore.collection("Offers").document(offerItem.postId).delete()
                            .continueWith {
                                firestore.collection("Offers").document("recentOffer").get().addOnSuccessListener {
                                    val offer=it.toObject(OfferRoomEntity::class.java)
                                    if (offer?.postId==offerItem.postId){
                                        firestore.collection("Offers").document("recentOffer").delete()
                                    }
                                }
                            }
                    }.addOnSuccessListener {
                        Toasty.success(requireActivity(),"Offer Deleted",Toasty.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                    }


                }
//
//
                .onNegative { _, _ ->

                }.show()





        }

        binding.EditOffer.setOnClickListener {

            val transition: Transition = Fade()
            transition.duration = 600
            transition.addTarget(binding.cardView)
            transition.addTarget(binding.cardView2)
            transition.addTarget(binding.EditOffer)
            transition.addTarget(binding.SaveOffer)

            TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
                binding.cardView.visibility=View.GONE
            TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
            binding.cardView2.visibility=View.VISIBLE
            TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
            binding.EditOffer.visibility=View.GONE
            TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
            binding.SaveOffer.visibility=View.VISIBLE


        }

        val localDatabase: AppDatabase = AppDatabase.getInstance(requireActivity())!!

        binding.SaveOffer.setOnClickListener {

            val map=HashMap<String,Any>()

            val title=binding.descriptionTextTitle.text.toString().trim()
            val descriptionTextMain=binding.descriptionTextMain.text.toString().trim()
//            val minPeople=binding.MinPeople.text.toString().trim()
            val discountedPrice=binding.DiscountedPriceEditText.text.toString().trim()
            val originalPrice= binding.OriginalPrice.text.toString().trim()
            map["discountPrice"]=discountedPrice
            map["originalPrice"] =originalPrice
            map["title"]=title
            map["description"]=descriptionTextMain

            firestore.collection("Offers").document(offerItem.postId).set(map, SetOptions.merge())
                .continueWith {
                firestore.collection("Offers").document("recentOffer").get().addOnSuccessListener {
                    val offer=it.toObject(OfferRoomEntity::class.java)
                    if (offer?.postId==offerItem.postId){
                        firestore.collection("Offers").document("recentOffer").set(map,
                            SetOptions.merge())
                    }
                }
            }.addOnSuccessListener {
                    firestore.collection("Offers").document(offerItem.postId).get().addOnSuccessListener {
                        if (it.exists()){
                            val offer:OfferRoomEntity?=it.toObject(OfferRoomEntity::class.java)
                            CoroutineScope(Dispatchers.IO).launch {
                                if (offer != null) {
                                    localDatabase.appDao()?.addOfferUser(offer)
                                }
                            }
                            if (offer != null) {
                                updateFieldsRecent(offer)
                            }

                        }
                    }.addOnFailureListener {
                        Log.d("MyTag",""+it.localizedMessage)
                    }
                    Toasty.success(requireActivity(),"Updated Successfully",Toasty.LENGTH_SHORT).show()
//                    activity?.onBackPressed()



                val transition: Transition = Fade()
                transition.duration = 600
                transition.addTarget(binding.cardView)
                transition.addTarget(binding.cardView2)
                transition.addTarget(binding.EditOffer)
                transition.addTarget(binding.SaveOffer)

                TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
                binding.cardView.visibility=View.VISIBLE
                TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
                binding.cardView2.visibility=View.GONE
                TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
                binding.EditOffer.visibility=View.VISIBLE
                TransitionManager.beginDelayedTransition(view!!.parent as ViewGroup,transition)
                binding.SaveOffer.visibility=View.GONE


            }.addOnFailureListener {
                Toasty.error(requireActivity(),"Error ",Toasty.LENGTH_SHORT).show()
            }



        }

        return binding.root
    }


    private fun updateFields(){

        if (offerItem != null) {
            Glide.with(requireActivity())
                .load(offerItem.image_url_0)
                .into(binding.img)
            Glide.with(requireActivity())
                .load(offerItem.thumbnailImage)
                .into(binding.profileImage)
        }
        if (offerItem.originalPrice.isNotBlank()&&offerItem.discountPrice.isNotBlank()){

            binding.DiscountedPrice.text="₹"+offerItem?.discountPrice
        }else if (offerItem.originalPrice.isBlank()){
            binding.DiscountedPrice.text="₹"+offerItem?.discountPrice
        }else if (offerItem.discountPrice.isBlank()){
            binding.DiscountedPrice.text="₹"+offerItem?.originalPrice
        }

        binding.DiscountedPriceEditText.setText(offerItem?.discountPrice)

        binding.username.text=offerItem?.username

        binding.offer.text=offerItem?.title
        binding.descriptionTextTitle.setText(offerItem?.title)

        binding.desc.text=offerItem?.description
        binding.descriptionTextMain.setText(offerItem?.description)

        binding.OriginalPrice.setText(offerItem?.originalPrice)

    }

    private fun updateFieldsRecent(offerItem:OfferRoomEntity){

        if (offerItem != null) {
            Glide.with(requireActivity())
                .load(offerItem.image_url_0)
                .into(binding.img)
            Glide.with(requireActivity())
                .load(offerItem.thumbnailImage)
                .into(binding.profileImage)
        }
        if (offerItem.originalPrice.isNotBlank()&&offerItem.discountPrice.isNotBlank()){

            binding.DiscountedPrice.text="₹"+offerItem?.discountPrice
        }else if (offerItem.originalPrice.isBlank()){
            binding.DiscountedPrice.text="₹"+offerItem?.discountPrice
        }else if (offerItem.discountPrice.isBlank()){
            binding.DiscountedPrice.text="₹"+offerItem?.originalPrice
        }

        binding.DiscountedPriceEditText.setText(offerItem?.discountPrice)

        binding.username.text=offerItem?.username

        binding.offer.text=offerItem?.title
        binding.descriptionTextTitle.setText(offerItem?.title)

        binding.desc.text=offerItem?.description
        binding.descriptionTextMain.setText(offerItem?.description)

        binding.OriginalPrice.setText(offerItem?.originalPrice)

    }


}