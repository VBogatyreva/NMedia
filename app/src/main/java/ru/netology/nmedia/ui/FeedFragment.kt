package ru.netology.nmedia.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.ui.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentFeedBinding

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = :: requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter (object : OnInteractionListener {
            override fun onLike(post: Post) {
                if(post.likedByMe){
                    viewModel.unlikeById(post.id)
                } else {
                    viewModel.likeById(post.id)
                }
            }
            override fun onShare(post: Post) {

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.share))
                startActivity(shareIntent)
            }
            override fun onSaw(post: Post) {
//                viewModel.sawById(post.id)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content.toString()
                    }
                )
            }

            override fun onVideo(post : Post) {
//                post.videoUrl?.let {viewModel.video()}
                if (!post.videoUrl.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    startActivity(intent)
                }
            }

            override fun onOpen(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_onePostFragment,
                    Bundle().apply
                    { textArg = post.id.toString() })
            }

            override fun getPostById(id: Long){
                viewModel.getPostById(id)
            }

            override fun onImage(post: Post) {
                if (post.attachment?.type == AttachmentType.IMAGE && !post.attachment.url.isNullOrEmpty()) {
                    viewModel.selectPost(post)
                    findNavController().navigate(R.id.action_feedFragment_to_imageFragment)
                } else {
                    Snackbar.make(
                        binding.root,
                        R.string.error_invalid_image,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })

        binding.list.adapter = adapter
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state.loading
            binding.errorGroup.isVisible = state.error

            if(state.error) {
                Snackbar.make(
                    binding.root,
                    state.messageCodeError,
                    BaseTransientBottomBar.LENGTH_LONG
                )
                    .setAnchorView(binding.fab)
                    .show()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0 && binding.list.isShown && adapter.itemCount > 0) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        })

        binding.newPosts.setOnClickListener {
            viewModel.loadVisiblePosts()
            binding.newPosts.isVisible = false
            binding.list.smoothScrollToPosition(0)
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }

    data class Post(
        val id: Long,
        val author: String,
        val authorId: Long,
        val published: String,
        val content: String,
        val authorAvatar: String,
        val likedByMe: Boolean,
        val likes: Long,
        val shares: Long,
        val visibility: Long,
        val videoUrl: String?,
        val hiddenPosts: Boolean,
        val attachment: Attachment? = null,
        val ownedByMe: Boolean = false
    )
    data class Attachment(
        val url: String,
        val type: AttachmentType
    )
}

object AndroidUnils {
    fun hideKeyboard (view : View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
}