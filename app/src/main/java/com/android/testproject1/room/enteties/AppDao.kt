package com.android.testproject1.room.enteties

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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

//    @Query("SELECT * FROM notes WHERE id= :id")
//    open fun getNoteById(id: Int): NoteEntity?
}