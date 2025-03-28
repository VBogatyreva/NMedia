package ru.netology.nmedia

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val authorAvatar: String,
    var likedByMe: Boolean,
    val likes: Long,
    val shares: Long,
    val visibility: Long,
    val videoUrl: String?,
    val hiddenPosts: Boolean = true,

    @Embedded
    val attachment: AttachmentEmbeddable?


) {
    fun toDto() = FeedFragment.Post(
        id = id,
        author = author,
        published = published,
        content = content,
        authorAvatar = authorAvatar,
        likedByMe = likedByMe,
        likes = likes,
        shares = shares,
        visibility = visibility,
        videoUrl = videoUrl,
        hiddenPosts = hiddenPosts,
        attachment = attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: FeedFragment.Post) = PostEntity(
            dto.id,
            dto.author,
            dto.published,
            dto.content,
            dto.authorAvatar,
            dto.likedByMe,
            dto.likes,
            dto.shares,
            dto.visibility,
            dto.videoUrl,
            dto.hiddenPosts,
            AttachmentEmbeddable.fromDto(dto.attachment)
            )

    }
}

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType
) {
    fun toDto() = FeedFragment.Attachment(url, type)

    companion object {
        fun fromDto(dto: FeedFragment.Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}

fun List<PostEntity>.toDto(): List<FeedFragment.Post> = map(PostEntity::toDto)
fun List<FeedFragment.Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
