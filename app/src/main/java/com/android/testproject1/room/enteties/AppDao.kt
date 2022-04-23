package com.android.testproject1.room.enteties

import androidx.lifecycle.LiveData
import androidx.room.*
import android.provider.SyncStateContract.Helpers.insert

import com.android.testproject1.model.Users




@Dao
interface AppDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertPost(PostEntity: PostRoomEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAllPosts(PostEntity :List<PostRoomEntity>)
//
//    @Query("Select * from PostRoomEntity")
//    fun getPosts(): LiveData<MutableList<PostRoomEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsersChatsList(UserChatEntity: MutableList<UsersChatListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsersChats(UserChatEntity: UsersChatListEntity)

    @Query("Select * from UsersChatListEntity")
    fun getUserChats(): LiveData<MutableList<UsersChatListEntity>>

    @Query("Delete from UsersChatListEntity")
    fun deleteAllChatList()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOffer(OfferEntity: OffersSavedRoomEntity)

    @Query("Select * from OffersSavedRoomEntity")
    fun getSavedOffers():LiveData<MutableList<OffersSavedRoomEntity>>

    @Query("Select * from OffersSavedRoomEntity WHERE postId = :postId")
    fun getSavedOffer(postId: String) : OffersSavedRoomEntity

    @Query("DELETE FROM OffersSavedRoomEntity WHERE postId = :postId")
    fun deleteOfferById(postId: String)

    @Query("DELETE FROM OffersSavedRoomEntity ")
    fun deleteTableOfferSavedRoomEntity()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOfferUser(OfferEntity: OfferRoomEntity)

    @Query("Select * from OfferRoomEntity where userId=:userId order by timestamp desc")
    fun getOffersUser(userId:String):LiveData<MutableList<OfferRoomEntity>>

    @Query("Select * from OfferRoomEntity order by timestamp desc")
    fun getAllOffers():LiveData<MutableList<OfferRoomEntity>>

    @Query("DELETE FROM OfferRoomEntity WHERE postId = :postId")
    fun deleteOfferByIdUser(postId: String)

    @Query("DELETE FROM OfferRoomEntity ")
    fun deleteTableOfferRoomEntity()

    @Transaction
    fun deleteAndInsert(OfferEntity: OfferRoomEntity) {
        deleteTableOfferRoomEntity()
        addOfferUser(OfferEntity)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(chatRoomEntity: ChatRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageList(chatRoomEntity: List<ChatRoomEntity>)

    @Query("Select * From ChatRoomEntity where chatKey= :chatKey order by timestamp ")
    fun getMessages(chatKey: String): LiveData<List<ChatRoomEntity>>

    @Query("Delete from ChatRoomEntity")
    fun deleteAllChatMessages()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(imageRoomEntity: UserImagesRoomEntity)

    @Query("Select thumbnailImage From UserImagesRoomEntity where userId= :userId")
    fun getUserImage(userId: String) :String

    @Query("Select username From UserImagesRoomEntity where userId= :userId")
    fun getUserName(userId: String) :String

    @Query("DELETE FROM UserImagesRoomEntity ")
    fun deleteTableUserImagesRoomEntity()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notificationRoomEntity: NotificationsRoomEntity)

    @Query("Select * From NotificationsRoomEntity order by timestamp desc")
    fun getNotifications(): LiveData<List<NotificationsRoomEntity>>

    @Query("Delete from NotificationsRoomEntity")
    fun deleteAllNotifications()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(orderRoomEntity: OrdersRoomEntity)

    @Query("Select * From OrdersRoomEntity order by timestamp desc")
    fun getOrders(): LiveData<List<OrdersRoomEntity>>

    @Query("DELETE FROM OrdersRoomEntity ")
    fun deleteTableOrdersRoomEntity()



//    @Query("Select * From ChatRoomEntity where chatKey= :chatKey order by timestamp ")
//    fun getMessagesList(chatKey: String): List<ChatRoomEntity>

    @Query("Select * From ChatRoomEntity where postId= :postId AND groupId= :groupId order by timestamp ")
    fun getGroupMessages(postId: String,groupId: String): LiveData<List<ChatRoomEntity>>

    @Query("Select * from ChatRoomEntity")
    fun getMessagesAll(): List<ChatRoomEntity>

    @Query("Delete from ChatRoomEntity")
    fun deleteAllMessages()


//    @Delete( "DELETE FROM Offer WHERE product_id=1")
//    fun removeOffer(OfferEntity: OffersSavedRoomEntity)

//    @Query("SELECT * FROM notes WHERE id= :id")
//    open fun getNoteById(id: Int): NoteEntity?
}