package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.ui.FeedFragment

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
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
    val ownedByMe: Boolean,

    @Embedded
    val attachment: AttachmentEmbeddable?


) {
    fun toDto() = FeedFragment.Post(
        id = id,
        authorId = authorId,
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
        attachment = attachment?.toDto(),
        ownedByMe = ownedByMe
    )

    companion object {
        fun fromDto(dto: FeedFragment.Post) = PostEntity(
            dto.id,
            dto.authorId,
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
            dto.ownedByMe,
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
fun List<FeedFragment.Post>.toEntity(): List<PostEntity> = map(PostEntity.Companion::fromDto)

fun PostEntity.toDto(): FeedFragment.Post = FeedFragment.Post(
    id = this.id,
    authorId = this.authorId,
    author = this.author,
    published = this.published,
    content = this.content,
    authorAvatar = this.authorAvatar,
    likedByMe = this.likedByMe,
    likes = this.likes,
    shares = this.shares,
    visibility = this.visibility,
    videoUrl = this.videoUrl,
    hiddenPosts = this.hiddenPosts,
    attachment = this.attachment?.let { attachment ->
        FeedFragment.Attachment(
            url = attachment.url,
            type = attachment.type
        )
    },
    ownedByMe = this.ownedByMe
)