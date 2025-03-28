package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentPost.observe(viewLifecycleOwner) { post ->
            post ?: return@observe

            post.attachment?.url?.let { url ->
                loadImage(url)
            } ?: run {binding.postImage.isVisible = false}

            updateLikeIcon(post.likedByMe)

            setupBottomAppBar(post)
        }
    }

    private fun loadImage(url: String) {
        Glide.with(binding.postImage)
                        .load("http://10.0.2.2:9999/media/$url")
                        .placeholder(R.drawable.baseline_loading_24)
                        .error(R.drawable.baseline_error_24)
                        .timeout(10_000)
                        .into(binding.postImage)
                    binding.postImage.isVisible = true
    }

    private fun updateLikeIcon(liked: Boolean) {
        binding.bottomAppBar.menu?.findItem(R.id.like)?.setIcon(
            if (liked) R.drawable.ic_launcher_liked_foreground
            else R.drawable.ic_launcher_like_foreground
        )
    }

    private fun setupBottomAppBar(post: FeedFragment.Post) {
        binding.bottomAppBar.setNavigationOnClickListener {
            viewModel.clearSelectedPost()
            findNavController().navigateUp()
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.like -> {
                    viewModel.likeById(post.id)
                    true
                }
                R.id.share -> {
                    sharePostContent(post.content)
                    true
                }
                else -> false
            }
        }
    }

    private fun sharePostContent(content: String) {
        val shareIntent = Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, content)
                type = "text/plain"
            },
            getString(R.string.share)
        )
        startActivity(shareIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
