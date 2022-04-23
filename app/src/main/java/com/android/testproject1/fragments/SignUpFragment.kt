package com.android.testproject1.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.MainActivity2
import com.android.testproject1.R
import com.android.testproject1.RegisterActivity
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.databinding.FragmentSignUpBinding
import com.android.testproject1.viewmodels.SignUpViewModel
import com.google.firebase.auth.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    private lateinit var mViewModel: SignUpViewModel
    private lateinit var binding:FragmentSignUpBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentSignUpBinding.inflate(inflater, container, false)

        binding.listener= context as IMainActivity

        mViewModel=ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))
            .get(SignUpViewModel::class.java)

        mViewModel.getUserLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
//                val intent = Intent(activity, MainActivity2::class.java)
//                startActivity(intent)
                val fragment:Fragment=UploadProfilePicFragment()
//                fragment.arguments=bundle

                val fragmentmanager= activity?.supportFragmentManager
                fragmentmanager!!
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .addToBackStack("Login Fragment")
                    .add(R.id.fragmentcontainer,fragment)
                    .commit();
                RegisterActivity.isUploadPicFragmentActive=true
            }
        })


        binding.signUp.setOnClickListener {
//            createUser()

            val username:String=binding.username.text.toString().trim()
            val email = signUpEmailID!!.text.toString().trim()
            val phoneNumber=signUpMobileNumber.text.toString().trim()
            val password1 = signUpPassword!!.text.toString().trim()
            val passwordConfirm=signUpConfirmPassword.text.toString().trim()

            if (username.isNotEmpty() && email.isNotEmpty()&& phoneNumber.isNotEmpty()&&password1.isNotEmpty()&&passwordConfirm.isNotEmpty()){
                if (password1 == passwordConfirm){
                    createUser()
                } else{
                    Toasty.error(requireActivity(),"Password doesn't match",Toasty.LENGTH_SHORT,true).show()
                }
            }else{
                Toasty.error(requireActivity(),"All Fields are required",Toasty.LENGTH_SHORT,true).show()
            }

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

        val username:String=binding.username.text.toString().trim()
        val email = signUpEmailID!!.text.toString().trim()
        val phoneNumber=signUpMobileNumber.text.toString().trim()
        val password1 = signUpPassword!!.text.toString().trim()
        mViewModel.register(email,password1,username,phoneNumber)

//        val bundle =Bundle()
//        bundle.putString("fullName",username)
//        bundle.putString("email",email)
//        bundle.putString("password1",password1)
//        bundle.putString("phoneNumber",phoneNumber)
//
//        val fragment:Fragment=UploadProfilePicFragment()
//        fragment.arguments=bundle
//
//        val fragmentmanager= activity?.supportFragmentManager
//        fragmentmanager!!
//            .beginTransaction()
//            .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//            .addToBackStack("Login Fragment")
//            .add(R.id.fragmentcontainer,fragment)
//            .commit();
//        RegisterActivity.isUploadPicFragmentActive=true

//        val funName:String=signUpFullName.text.toString().trim()
//        val email = signUpEmailID!!.text.toString().trim()
//        val phoneNumber=signUpMobileNumber.text.toString().trim()
//        val password1 = signUpPassword!!.text.toString().trim()
//        val passwordConfirm=signUpConfirmPassword.text.toString().trim()
//
//        mViewModel.register(email, password1,funName)

    }


}