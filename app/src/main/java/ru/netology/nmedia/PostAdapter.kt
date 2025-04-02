package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.CardPostBinding

interface OnInteractionListener {
    fun onLike (post : FeedFragment.Post) {}
    fun onEdit (post : FeedFragment.Post) {}
    fun onRemove (post : FeedFragment.Post) {}
    fun onShare (post : FeedFragment.Post) {}
    fun onSaw (post : FeedFragment.Post) {}
    fun onVideo (post : FeedFragment.Post) {}
    fun onOpen (post : FeedFragment.Post) {}
    fun getPostById(id: Long)
    fun onImage(post: FeedFragment.Post)

}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener

) : ListAdapter<FeedFragment.Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {


    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder (holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class PostViewHolder (
        private val binding: CardPostBinding,
        private val onInteractionListener: OnInteractionListener

    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind (post: FeedFragment.Post) {
            binding.apply {
                author.text = post.author
                published.text = post.published
                content.text = post.content

                Glide.with(binding.avatar)
                    .load("http://10.0.2.2:9999/avatars/${post.authorAvatar}")
                    .circleCrop()
                    .placeholder(R.drawable.baseline_loading_24)
                    .error(R.drawable.baseline_error_24)
                    .timeout(10_000)
                    .into(binding.avatar)

                likes.text = numberFormat(post.likes)
                share.text = numberFormat(post.shares)
                visibiluty.text = numberFormat(post.visibility)

                likes.isChecked = post.likedByMe
                likes.text = "${post.likes}"

                if (!post.videoUrl.isNullOrEmpty()) {
                    videoLayout.visibility = View.VISIBLE
                } else {
                    videoLayout.visibility = View.GONE
                }

                videoLayout.setOnClickListener {
                    onInteractionListener.onVideo(post)
                }

                likes.setOnClickListener{
                    onInteractionListener.onLike(post)
                }

                share.setOnClickListener {
                    onInteractionListener.onShare(post)
                }

                visibiluty.setOnClickListener {
                    onInteractionListener.onSaw(post)
                }

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }

                postImage.visibility = if (post.attachment?.type == AttachmentType.IMAGE && !post.attachment.url.isNullOrEmpty()) View.VISIBLE else View.GONE
                postImage.setOnClickListener {
                    if (post.attachment?.type == AttachmentType.IMAGE && !post.attachment.url.isNullOrEmpty()) {
                        onInteractionListener.onImage(post)
                    } else {
                        Snackbar.make(
                            binding.root,
                            R.string.error_invalid_image,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                root.setOnClickListener {
                    onInteractionListener.onOpen(post)
                }
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<FeedFragment.Post>() {
        override fun areItemsTheSame(oldItem: FeedFragment.Post, newItem: FeedFragment.Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FeedFragment.Post, newItem: FeedFragment.Post): Boolean {
            return oldItem == newItem
        }
    }
}

