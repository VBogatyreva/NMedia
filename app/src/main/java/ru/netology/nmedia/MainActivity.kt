package ru.netology.nmedia

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding

// branch master 2.4. CRUD

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter (object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
            override fun onSaw(post: Post) {
                viewModel.sawById(post.id)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.save.setOnClickListener {
            with (binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText (
                        this@MainActivity,
                        "Content can`t be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUnils.hideKeyboard(this)
                binding.group.visibility = View.GONE

            }
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
                binding.group.visibility = View.VISIBLE
            }

        }

        binding.content.setOnClickListener{
            binding.group.visibility = View.VISIBLE
        }

        binding.cancel.setOnClickListener {
            with(binding.content) {

                viewModel.cancel(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUnils.hideKeyboard(this)
                binding.group.visibility = View.GONE
            }
        }
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
        val visibility: Long
    )
}

object AndroidUnils {
    fun hideKeyboard (view : View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
}






