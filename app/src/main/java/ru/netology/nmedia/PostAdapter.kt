package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.NetologyMainBinding


typealias OnLikeListener = (post : MainActivity.Post) -> Unit
typealias OnShareListener = (post : MainActivity.Post) -> Unit
typealias OnSawListener = (post : MainActivity.Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener,
    private val onSawListener: OnSawListener

) : ListAdapter<MainActivity.Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {


    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = NetologyMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener, onSawListener)
    }

    override fun onBindViewHolder (holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }


    class PostViewHolder (
        private val binding: NetologyMainBinding,
        private val onLikeListener: OnLikeListener,
        private val onShareListener: OnShareListener,
        private val onSawListener: OnSawListener

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
                    onLikeListener(post)
                }

                share.setOnClickListener {
                    onShareListener(post)
                }

                visibiluty.setOnClickListener {
                    onSawListener(post)
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