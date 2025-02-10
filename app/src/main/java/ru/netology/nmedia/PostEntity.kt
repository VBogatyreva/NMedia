package ru.netology.nmedia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likedByMe: Boolean,
    val likes: Long,
    val shares: Long,
    val visibility: Long,
    val videoUrl: String?
) {

    companion object {
        fun fromDto(dto: FeedFragment.Post) = with(dto) {
            PostEntity(
                id = id,
                author = author,
                published = published,
                content = content,
                likedByMe = likedByMe,
                likes = likes,
                shares = shares,
                visibility = visibility,
                videoUrl = videoUrl
            )
        }
    }

//    fun toDto() = FeedFragment.Post(
//        id = id,
//        author = author,
//        published = published,
//        content = content,
//        likedByMe = likedByMe,
//        likes = likes,
//        shares = shares,
//        visibility = visibility,
//        videoUrl = videoUrl
//    )

}
