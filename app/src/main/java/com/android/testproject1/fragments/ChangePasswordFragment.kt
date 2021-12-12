package com.android.testproject1.fragments

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentChangePasswordBinding
import com.android.testproject1.databindingadapters.bind
import com.google.firebase.auth.EmailAuthProvider

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty


class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    //    private lateinit var mViewModel:
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.changePassword.setOnClickListener {

            val email = binding.email.text.toString().trim()
            val oldPassword = binding.oldPassword.text.toString().trim()
            val newPassword = binding.newPassword.text.toString().trim()
            if (email.isNotEmpty()&&oldPassword.isNotEmpty()&&newPassword.isNotEmpty()){

                changePassword(email,oldPassword,newPassword)
            }
        }

        return binding.root
    }

    private fun changePassword(email: String, oldPassword: String, newPassword: String) {

        val firebaseUser = firebaseAuth.currentUser
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        firebaseUser?.reauthenticate(credential)?.addOnSuccessListener {
            firebaseUser.updatePassword(newPassword).addOnSuccessListener {
                Toasty.success(activity!!,"Password Changed",Toasty.LENGTH_SHORT,true).show()
            }.addOnFailureListener {
                Toasty.error(activity!!,"Error Updating Password",Toasty.LENGTH_SHORT,true).show()
            }
        }?.addOnFailureListener {
            Log.d("MyTag", "error is : ${it.localizedMessage}")
            Toasty.error(activity!!,"Authentication Error",Toasty.LENGTH_SHORT,true).show()
        }

    }
}