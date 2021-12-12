package com.android.testproject1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentSearchPeopleContainerBinding
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Users

class SearchPeopleContainer : Fragment() {

    private lateinit var binding:FragmentSearchPeopleContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSearchPeopleContainerBinding.inflate(inflater,container,false)

        val bundle = this.arguments
        val stringCheck= bundle?.getString("openFriend")

        Log.d("MyTag", "open friend is : $stringCheck")

        if (stringCheck=="openFriend"){

            val notificationsItem= bundle.getParcelable<NotificationsRoomEntity>("notificationsItem")
            val id :String = notificationsItem?.id.toString()
            Log.d("MyTag", "id is : $id")

            val fragment= ProfileOpened()
            val bundle2 = Bundle()
            bundle2.putString("openFriend","openFriend")
            bundle2.putString("iD",id)
            fragment.arguments=bundle2
            loadFragment(fragment)

        }else if(stringCheck=="openFriendSearch"){

            val userItem= bundle.getParcelable<Users>("userItem")
            val id :String = userItem?.userId.toString()
            Log.d("MyTag", "id is : $id")

            val fragment= ProfileOpened()
            val bundle2 = Bundle()
            bundle2.putString("openFriend","openFriend")
            bundle2.putString("iD",id)
            fragment.arguments=bundle2
            loadFragment(fragment)

        }else{
            loadFragment(SearchPeople())
        }

//        loadFragment(SearchPeople())


        return binding.root
    }

    private fun loadFragment(fragment: Fragment?) {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_container, fragment!!)
            .commit()
    }

}