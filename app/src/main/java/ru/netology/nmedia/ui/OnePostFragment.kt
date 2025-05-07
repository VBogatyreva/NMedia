package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.databinding.FragmentOnePostBinding

@AndroidEntryPoint
class OnePostFragment : Fragment() {

    val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentOnePostBinding.inflate(inflater, container, false)

//        val viewHolder = PostsAdapter.PostViewHolder(binding.post,
//            object : OnInteractionListener {
//                override fun onLike(post: FeedFragment.Post) {
//                    viewModel.likeById(post.id)
//                }
//                override fun onShare(post: FeedFragment.Post) {
//                    viewModel.shareById(post.id)
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(Intent.EXTRA_TEXT, post.content)
//                        type = "text/plain"
//                    }
//                    val shareIntent = Intent.createChooser(intent, getString(R.string.share))
//                    startActivity(shareIntent)
//                }
//                override fun onSaw(post: FeedFragment.Post) {
//                    viewModel.sawById(post.id)
//                }
//                override fun onRemove(post: FeedFragment.Post) {
//                    viewModel.removeById(post.id)
//                }
//                override fun onEdit(post: FeedFragment.Post) {
//                    viewModel.edit(post)
//
//                }
//
//                override fun onVideo(post : FeedFragment.Post) {
//                    post.videoUrl?.let {viewModel.video()}
//                    if (!post.videoUrl.isNullOrEmpty()) {
//                        val intent = Intent(Intent.ACTION_VIEW, parse(post.videoUrl))
//                        startActivity(intent)
//                    }
//                }
//
//            })
//
//        viewModel.edited.observe(viewLifecycleOwner) {
//            if(it.id != 0L) {
//                findNavController().navigate(R.id.action_onePostFragment_to_newPostFragment, Bundle().apply { textArg = it.content })
//
//            }
//        }
//
//        val id = arguments?.textArg?.toLong() ?: -1
//
////        viewModel.data.observe(viewLifecycleOwner) { posts ->
////            val post = posts.find { it.id == id } ?: run{
////
////                findNavController().navigateUp()
////                return@observe
////            }
////
////            viewHolder.bind(post)
////        }

        return binding.root
    }

}