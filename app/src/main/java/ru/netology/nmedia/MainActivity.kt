package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.NetologyMainBinding

// branch master



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = NetologyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter (
            { viewModel.likeById(it.id) },
            { viewModel.shareById(it.id) },
            { viewModel.sawById(it.id) }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.post)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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






