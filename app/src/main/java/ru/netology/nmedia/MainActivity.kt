package ru.netology.nmedia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.activity.EditPostResultContract
import ru.netology.nmedia.activity.NewPostResultContract
import ru.netology.nmedia.databinding.ActivityMainBinding

// branch master 3.2. Организация навигации (перемещение между Activity)

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val editPostLauncher = registerForActivityResult (EditPostResultContract()) { result ->
            result?.let {
                viewModel.changeContent(result)
                viewModel.save()
            }
            viewModel.clearEdit()

        }

        val adapter = PostsAdapter (object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.share))
                startActivity(shareIntent)
            }
            override fun onSaw(post: Post) {
                viewModel.sawById(post.id)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)

            }

            override fun onVideo(post : Post) {
                post.videoUrl?.let {viewModel.video()}
                if (!post.videoUrl.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    startActivity(intent)
                }
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        val newPostLauncher = registerForActivityResult (NewPostResultContract()) { result ->
            result?:return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }

//        val editPostLauncher = registerForActivityResult (EditPostResultContract()) { result ->
//            result?:return@registerForActivityResult
//            viewModel.changeContent(result)
//            viewModel.save()
//        }



//        viewModel.edited.observe(this) {
//            if (it.id == 0L) {
//                return@observe
//            }
//            editPostLauncher.launch(it.content)
//        }

    }

    data class Post(
        val id: Long,
        val author: String,
        val published: String,
        val content: String,
        val likedByMe: Boolean,
        val likes: Long,
        val sharedByMe: Boolean,
        val shares: Long,
        val sawByMe: Boolean,
        val visibility: Long,
        val videoUrl: String?
    )
}

object AndroidUnils {
    fun hideKeyboard (view : View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }



}






