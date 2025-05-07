package ru.netology.nmedia.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, PostEntity>

    @Query("SELECT MIN(id) FROM PostEntity")
    suspend fun getFirstId(): Long?

    @Query("SELECT id FROM PostEntity")
    suspend fun getAllIds(): List<Long>

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll() : Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    suspend fun getById(id: Long): PostEntity

    @Query("SELECT * FROM PostEntity WHERE hiddenPosts=0 ORDER BY id DESC")
    fun getAllVisible(): Flow<List<PostEntity>>

    @Query("UPDATE PostEntity SET hiddenPosts = 0 ")
    suspend fun showAll()

    @Insert(onConflict = REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    suspend fun updateContentById(id: Long, content: String)

    @Query("""
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    suspend fun likeById(id:Long)

    @Query("""
        UPDATE PostEntity SET
        shares = shares + 1
        WHERE id = :id
        """)
    suspend fun shareById(id:Long)


    @Query("""
        UPDATE PostEntity SET
        visibility = visibility + 1
        WHERE id = :id
        """)
    suspend fun sawById(id:Long)


    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id:Long)

    @Query("DELETE FROM PostEntity")
    suspend fun clear()

    suspend fun save(post: PostEntity) =
        if(post.id == 0L) insert(post) else updateContentById(post.id, post.content)

}

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}