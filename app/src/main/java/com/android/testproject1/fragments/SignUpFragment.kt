package com.android.testproject1.fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.IMainActivity
import com.android.testproject1.MainActivity
import com.android.testproject1.MainActivity2
import com.android.testproject1.databinding.FragmentSignUpBinding
import com.android.testproject1.viewmodels.LoginViewModel
import com.android.testproject1.viewmodels.SignUpViewModel
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    private lateinit var mViewModel: SignUpViewModel

    //    var imageUri: Uri? = null
    private val myTAG:String="MyTag"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding= FragmentSignUpBinding.inflate(inflater, container, false)

        binding.listener= context as IMainActivity

        mViewModel=ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))
            .get(SignUpViewModel::class.java)

        mViewModel.getUserLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val intent = Intent(activity, MainActivity2::class.java)
                startActivity(intent)
            }
        })


        binding.signUp.setOnClickListener {
            createUser()
        }

        return binding.root

    }

    private fun createUser() {
        if (!mViewModel.validateEmailAddress(signUpEmailID) or !mViewModel.validatePassword(signUpPassword))
        {
            // Email or Password not valid,
            return
        }
        //Email and Password valid, create user here

        val funName:String=signUpFullName.text.toString().trim()
        val email = signUpEmailID!!.text.toString().trim()
        val phoneNumber=signUpMobileNumber.text.toString().trim()
        val password1 = signUpPassword!!.text.toString().trim()
        val passwordConfirm=signUpConfirmPassword.text.toString().trim()

        mViewModel.register(email, password1,funName)

    }


}