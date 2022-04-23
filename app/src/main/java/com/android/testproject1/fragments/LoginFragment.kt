package com.android.testproject1.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.MainActivity2
import com.android.testproject1.R
import com.android.testproject1.RegisterActivity
import com.android.testproject1.databinding.FragmentLoginBinding
import com.android.testproject1.sealedclasses.LoginUiStates
import com.android.testproject1.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var mViewModel: LoginViewModel
    private lateinit var shakeAnimation: Animation
    val mYTag:String="MyTag"

    private lateinit var sharedPrefDynamic: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding:FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
//        context?.theme?.applyStyle(R.style.AppTheme, true);

        binding= FragmentLoginBinding.inflate(inflater, container, false)
        binding.listener = context as IMainActivity

        mViewModel=ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(LoginViewModel::class.java)

        firebaseAuth= FirebaseAuth.getInstance()
        val currentUserID=firebaseAuth.currentUser?.uid.toString()
        sharedPrefDynamic= activity?.getSharedPreferences(currentUserID, Context.MODE_PRIVATE)!!
        val profileImage= sharedPrefDynamic.getString("profileimage","")

        mViewModel.uiState.observe(viewLifecycleOwner){
            when(it){

                is LoginUiStates.Error -> error()
                LoginUiStates.Loading -> loading()
                is LoginUiStates.Success -> loginSuccessful()
            }
        }

        mViewModel.getUserLiveData().observe(viewLifecycleOwner, { firebaseUser ->
            mViewModel.getUserProfileImage().observe(viewLifecycleOwner,{
                if (firebaseUser != null) {
                    if (it != null) {
                        if (it.isNotBlank()) {
                            Log.d(mYTag, "profile image is : $profileImage")
                            val intent = Intent(activity, MainActivity2::class.java)
                            startActivity(intent)
                            activity?.finish() }
                        }else{
                            val fragment:Fragment=UploadProfilePicFragment()
                            val fragmentmanager= activity?.supportFragmentManager
                            fragmentmanager!!
                                .beginTransaction()
                                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                                .addToBackStack("Login Fragment")
                                .add(R.id.fragmentcontainer,fragment)
                                .commit();
                            RegisterActivity.isUploadPicFragmentActive=true
                        }

                }
            })

        })

//         Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(activity, R.anim.shake)

        binding.loginBtn.setOnClickListener {
            signInUser(binding)
        }



        return binding.root
    }

    private fun loginSuccessful() {

    }

    private fun error() {
        binding.contentLayout.alpha = 1F;
        binding.bottomLayout.alpha=0.1F
        binding.contentLayout.isEnabled=true
        binding.bottomLayout.isEnabled=true
        binding.lottiView.visibility=View.GONE
    }

    private fun loading() {
        binding.contentLayout.alpha = 0.5F
        binding.bottomLayout.alpha=0.5F
        binding.bottomLayout.isEnabled=false
        binding.contentLayout.isEnabled=false
        binding.lottiView.visibility=View.VISIBLE
    }

    private fun signInUser(binding: FragmentLoginBinding) {

        if (!mViewModel.validateEmailAddress(login_emailid) or !mViewModel.validatePassword(login_password)) {
            // Email or Password not valid,
            binding.rlFooter.startAnimation(shakeAnimation)
            return
        }
        //Email and Password valid, sign in user here

        val email = login_emailid.text.toString().trim()
        val password1 = login_password!!.text.toString().trim()
        //showProgressBar();

        mViewModel.login(email, password1)


    }

}



