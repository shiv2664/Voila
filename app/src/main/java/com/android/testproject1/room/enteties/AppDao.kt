package com.android.testproject1.room.enteties

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.testproject1.fragments.GroupsOnOfferFragment

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(PostEntity: PostRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPosts(PostEntity :List<PostRoomEntity>)

    @Query("Select * from PostRoomEntity")
    fun getPosts(): LiveData<MutableList<PostRoomEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsersChats(UserChatEntity: MutableList<UsersRoomEntity>)

    @Query("Select * from UsersRoomEntity")
    fun getUserChats(): LiveData<MutableList<UsersRoomEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOffer(OfferEntity: OffersSavedRoomEntity)

    @Query("Select * from OffersSavedRoomEntity")
    fun getSavedOffers():LiveData<MutableList<OffersSavedRoomEntity>>

    @Query("DELETE FROM OffersSavedRoomEntity WHERE postId = :postId")
    fun deleteOfferById(postId: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(chatRoomEntity: ChatRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageList(chatRoomEntity: List<ChatRoomEntity>)

    @Query("Select * From ChatRoomEntity where chatKey= :chatKey order by timestamp")
    fun getMessages(chatKey: String): LiveData<MutableList<ChatRoomEntity>>

    @Query("Select * from ChatRoomEntity")
    fun getMessagesAll(): List<ChatRoomEntity>

    @Query("Delete from ChatRoomEntity")
    fun deleteAllMessages()


//    @Delete( "DELETE FROM Offer WHERE product_id=1")
//    fun removeOffer(OfferEntity: OffersSavedRoomEntity)

//    @Query("SELECT * FROM notes WHERE id= :id")
//    open fun getNoteById(id: Int): NoteEntity?
}