package com.android.testproject1.fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.IMainActivity
import com.android.testproject1.MainActivity
import com.android.testproject1.MainActivity2
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentLoginBinding
import com.android.testproject1.viewmodels.LoginViewModel

import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var mViewModel: LoginViewModel
    private lateinit var shakeAnimation: Animation
    val mYTag:String="MyTag"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
//        context?.theme?.applyStyle(R.style.AppTheme, true);

        val binding= FragmentLoginBinding.inflate(inflater, container, false)
        binding.listener = context as IMainActivity

        mViewModel=ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(LoginViewModel::class.java)
//        requireActivity().application

        mViewModel.getUserLiveData().observe(viewLifecycleOwner, {
            if (it != null) {
//                Log.d(mYTag, it.toString())
                activity?.finish()
                val intent = Intent(activity, MainActivity2::class.java)
                startActivity(intent)
            }
        })

//         Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(activity, R.anim.shake)

        binding.loginBtn.setOnClickListener {
            signInUser(binding)
        }

        return binding.root
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



