package com.android.testproject1

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.android.testproject1.databinding.ActivityRegisterBinding
import com.android.testproject1.databinding.FragmentHomeBinding
import com.android.testproject1.databinding.FragmentLoginBinding
import com.android.testproject1.databinding.FragmentSignUpBinding
import com.android.testproject1.fragments.LoginFragment
import com.android.testproject1.fragments.SignUpFragment
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.PostRoomEntity
import com.android.testproject1.room.enteties.UsersRoomEntity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class RegisterActivity : AppCompatActivity(),IMainActivity {
    private var fragmentmanager:FragmentManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fragmentmanager= supportFragmentManager
        fragmentmanager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_enter,R.anim.left_out)
                .add(R.id.fragmentcontainer, LoginFragment())
                .commit()

        textAnim()

    }

    private fun textAnim(){
        val view=findViewById<View>(R.id.textView1)
        ObjectAnimator.ofFloat(view,View.ALPHA,0f,1f).apply { duration=2000 }.start()
//        val view2=findViewById<View>(R.id.textView2)
//        ObjectAnimator.ofFloat(view2,View.ALPHA,0f,1f).apply { duration=2000 }.start()

    }

    override fun onRegisterClick() {
        fragmentmanager= supportFragmentManager

        fragmentmanager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter,R.anim.left_out)
                .addToBackStack("Login Fragment")
                .replace(R.id.fragmentcontainer, SignUpFragment(),"SignUp Fragment")
                .commit();
        textView1.text = "Hello there,"
        textView2.text="SignUp to get started"
        textAnim()
    }

    override fun onRecyclerViewItemClick(postItem: Post) {
        TODO("Not yet implemented")
    }

    override fun onJoinItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: UsersRoomEntity) {
    }

    override fun onGroupItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    private fun replaceLoginFragment() {
        fragmentmanager!!.popBackStack()
        fragmentmanager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter,R.anim.right_out)
                .replace(R.id.fragmentcontainer, LoginFragment())
                .commit()
        textView1.text = "Welcome Back"
        textView2.text="We missed you! Login to get started"
        textAnim()
    }
    override fun onBackPressed() {
        val signUpFragment = fragmentmanager?.findFragmentByTag("SignUp Fragment")
        if (signUpFragment != null) {
            replaceLoginFragment()
        }
        else super.onBackPressed()
    }
}