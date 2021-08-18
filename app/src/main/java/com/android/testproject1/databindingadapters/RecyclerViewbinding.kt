package com.android.testproject1.databindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*
import com.android.testproject1.adapter.*
import com.android.testproject1.model.*
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.android.testproject1.room.enteties.UsersRoomEntity


@BindingAdapter("bindhomerecyclerview")
fun bindHomeRecyclerView(view: RecyclerView, dataList:List<Post>?) {

    if (dataList != null) {
        if (dataList.isEmpty())
            return
    }

//    val layoutManager = view.layoutManager
//    if (layoutManager == null)
//        view.layoutManager = LinearLayoutManager(view.context)
//
//    var adapter = view.adapter
//
//    if (adapter == null) {
//        if (dataList != null) {
//            adapter = PostsAdapter2(view.context, dataList.toMutableList())
//        }
//        view.adapter = adapter
//    }

    val linearLayoutManager = LinearLayoutManager(view.context)
    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:PostsAdapter2?= view.adapter as PostsAdapter2?

    if (adapter == null) {
        if (dataList != null) {
            adapter = PostsAdapter2(view.context,dataList.toMutableList())
            view.adapter = adapter
        }
    }
    if (dataList != null) {
        adapter?.updateData(dataList)
    }

}


@BindingAdapter("bindnotificationrecyclerview")
fun bindNotificationsRecyclerView(view: RecyclerView, dataList:List<Notifications>?) {

//    if (dataList != null) {
//        if (dataList.isEmpty())
//            return
//    }

    val linearLayoutManager = LinearLayoutManager(view.context)
    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    view.itemAnimator = DefaultItemAnimator()
//    view.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
    view.setHasFixedSize(true)

    var adapter:NotificationsAdapter?= view.adapter as NotificationsAdapter?

    if (adapter == null) {
        if (dataList != null) {
            adapter = NotificationsAdapter(view.context,dataList.toMutableList())
            view.adapter = adapter
        }
    }
    if (dataList != null) {
        adapter?.updateData(dataList)
    }

}



@BindingAdapter("bindprofilepostrecyclerview")
fun bindRecyclerView(view: RecyclerView, dataList:List<Post>?) {

    if (dataList != null) {
        if (dataList.isEmpty())
            return
    }

    val gridLayoutManager = GridLayoutManager(view.context,3)
    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = gridLayoutManager
    }

    var adapter:ProfilePostRecyclerViewAdapter?= view.adapter as ProfilePostRecyclerViewAdapter?

    if (adapter == null) {
        if (dataList != null) {
            adapter = ProfilePostRecyclerViewAdapter(view.context,dataList.toMutableList())
            view.adapter = adapter
        }
    }
    if (dataList != null) {
        adapter?.updateData(dataList)
    }

}



@BindingAdapter("bindDetailsRecyclerView")
fun bindList(view: RecyclerView,usersList: List<Users>?){

    if (usersList != null) {
        if (usersList.isEmpty())
            return
    }
    val linearLayoutManager = LinearLayoutManager(view.context)
    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:GroupsOnOfferAdapter?= view.adapter as GroupsOnOfferAdapter?

    if (adapter == null) {
        if (usersList != null) {
            adapter = GroupsOnOfferAdapter(view.context,usersList.toMutableList())
            view.adapter = adapter
        }
    }
    if (usersList != null) {
        adapter?.updateData(usersList)
    }

}


@BindingAdapter("bindSearchPeopleRecyclerview")
fun bindPeopleSearchList(view: RecyclerView,usersList: List<Users>?){

    if (usersList != null) {
        if (usersList.isEmpty())
            return
    }
    val linearLayoutManager = LinearLayoutManager(view.context)
    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:SearchPeopleAdapter?= view.adapter as SearchPeopleAdapter?

    if (adapter == null) {
        if (usersList != null) {
            adapter = SearchPeopleAdapter(view.context,usersList.toMutableList())
            view.adapter = adapter
        }
    }
    if (usersList != null) {
        adapter?.updateData(usersList)
    }

}


@BindingAdapter("bindGroupList")
fun bind(view: RecyclerView,usersList: List<Users>?){

    if (usersList != null) {
        if (usersList.isEmpty())
            return
    }

    val linearLayoutManager = LinearLayoutManager(view.context)

    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:GroupAdapter?= view.adapter as GroupAdapter?

    if (adapter == null) {
        if (usersList != null) {
            adapter = GroupAdapter(view.context,usersList.toMutableList())
            view.adapter = adapter
        }
    }
    if (usersList != null) {
        adapter?.updateData(usersList)
    }

}


@BindingAdapter("bindUserChatList")
fun bindUserChatList(view: RecyclerView,usersList: List<UsersRoomEntity>?){

    if (usersList != null) {
        if (usersList.isEmpty())
            return
    }

    val linearLayoutManager = LinearLayoutManager(view.context)

    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:UserChatListAdapter?= view.adapter as UserChatListAdapter?

    if (adapter == null) {
        if (usersList != null) {
            adapter = UserChatListAdapter(view.context,usersList.toMutableList())
            view.adapter = adapter
        }
    }
    if (usersList != null) {
        adapter?.updateData(usersList)
    }

}


@BindingAdapter("bindChat")
fun bindChat(view:RecyclerView,chatList:List<ChatRoomEntity>?) {
    if (chatList != null) {
        if (chatList.isEmpty())
            return
    }

    val linearLayoutManager = LinearLayoutManager(view.context)
    linearLayoutManager.stackFromEnd = true

    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:ChatAdapter?= view.adapter as ChatAdapter?

    if (adapter == null) {
        if (chatList != null) {
            adapter = ChatAdapter(view.context,chatList.toMutableList())
            view.adapter = adapter
        }
    }
    if (chatList != null) {
        adapter?.updateData(chatList)
        if (adapter != null) {
            view.scrollToPosition(adapter.itemCount -1)
        }
    }

}


@BindingAdapter("bindGroupChat")
fun bindGroupChat(view:RecyclerView,groupChatList:List<ChatRoomEntity>?) {
    if (groupChatList != null) {
        if (groupChatList.isEmpty())
            return
    }

    val linearLayoutManager = LinearLayoutManager(view.context)
    linearLayoutManager.stackFromEnd = true

    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:ChatGroupAdapter?= view.adapter as ChatGroupAdapter?

    if (adapter == null) {
        if (groupChatList != null) {
            adapter = ChatGroupAdapter(view.context,groupChatList.toMutableList())
            view.adapter = adapter
        }
    }
    if (groupChatList != null) {
        adapter?.updateData(groupChatList)
        if (adapter != null) {
            view.scrollToPosition(adapter.itemCount -1)
        }
    }

}


@BindingAdapter("bindSearchRecyclerView")
fun bindSearchRecyclerView(view: RecyclerView,dataList:List<Offer>?){

    if (dataList != null) {
        if (dataList.isEmpty())
            return
    }

    val linearLayoutManager = LinearLayoutManager(view.context)
    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:SearchAdapter2?= view.adapter as SearchAdapter2?

    if (adapter == null) {
        if (dataList != null) {
            adapter = SearchAdapter2(view.context,dataList.toMutableList())
            view.adapter = adapter
        }
    }
    if (dataList != null) {
        adapter?.updateData(dataList)
    }


}


@BindingAdapter("bindProfileSavedRecyclerView")
fun bindProfileSavedRecyclerView(view: RecyclerView,dataList:List<OffersSavedRoomEntity>?){

    if (dataList != null) {
        if (dataList.isEmpty())
            return
    }

    val linearLayoutManager = LinearLayoutManager(view.context)
    val layoutManager = view.layoutManager
    if (layoutManager == null) {
        view.layoutManager = linearLayoutManager
    }

    var adapter:SavedOffersAdapter?= view.adapter as SavedOffersAdapter?

    if (adapter == null) {
        if (dataList != null) {
            adapter = SavedOffersAdapter(view.context,dataList.toMutableList())
            view.adapter = adapter
        }
    }
    if (dataList != null) {
        adapter?.updateData(dataList)
    }


}




//    var adapter:SearchAdapter2?= view.adapter as SearchAdapter2?
//
//    if (adapter == null) {
//        if (productExplore != null) {
//            adapter = SearchAdapter2(view.context,productExplore.toMutableList())
//            view.adapter = adapter
//        }
//    }
//    if (productExplore != null) {
//        adapter?.updateData(productExplore)
//    }

//    val layoutManager = view.layoutManager
//    if (layoutManager == null)
//        view.layoutManager = LinearLayoutManager(view.context)
//
//    var adapter = view.adapter
//
//
//    if (adapter == null) {
//        if (usersList != null) {
//            adapter = GroupAdapter(view.context,usersList.toMutableList())
//            view.adapter?.notifyDataSetChanged()
//
//        }
//        view.adapter = adapter
//
//    }