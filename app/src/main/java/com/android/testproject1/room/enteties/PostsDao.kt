package com.android.testproject1.room.enteties

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.testproject1.model.Post

@Dao
interface PostsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(PostEntity :PostRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPosts(PostEntity :List<PostRoomEntity>)

    @Query("Select * from PostRoomEntity")
    fun getPosts(): MutableLiveData<MutableList<PostRoomEntity>> = MutableLiveData()

//    @Query("SELECT * FROM notes WHERE id= :id")
//    open fun getNoteById(id: Int): NoteEntity?
}