package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding

interface OnInteractionListener {
    fun onLike (post : MainActivity.Post) {}
    fun onEdit (post : MainActivity.Post) {}
    fun onRemove (post : MainActivity.Post) {}
    fun onShare (post : MainActivity.Post) {}
    fun onSaw (post : MainActivity.Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener

) : ListAdapter<MainActivity.Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {


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
        fun bind (post: MainActivity.Post) {
            binding.apply {
                author.text = post.author
                published.text = post.published
                content.text = post.content

                countLikes.text = numberFormat(post.likes)
                countSharing.text = numberFormat(post.shares)
                countVisibility.text = numberFormat(post.visibility)

                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_launcher_liked_foreground else R.drawable.ic_launcher_like_foreground
                )

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
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<MainActivity.Post>() {
        override fun areItemsTheSame(oldItem: MainActivity.Post, newItem: MainActivity.Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MainActivity.Post, newItem: MainActivity.Post): Boolean {
            return oldItem == newItem
        }
    }
}