package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
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

         val viewModel : PostRepositoryInMemoryImpl.PostViewModel by viewModels()
         viewModel.data.observe(this) { post ->
             with(binding) {
                 author.text = post.author
                 published.text = post.published
                 content.text = post.content

                 countLikes.text = numberFormat(post.likes)
                 countSharing.text = numberFormat(post.shares)
                 countVisibility.text = numberFormat(post.visibility)

                 likes.setImageResource(
                     if (post.likedByMe) R.drawable.ic_launcher_liked_foreground else R.drawable.ic_launcher_like_foreground
                 )
             }
         }

         binding.likes.setOnClickListener {
             viewModel.like()
         }

         binding.share.setOnClickListener {
             viewModel.share()
         }

         binding.visibiluty.setOnClickListener {
             viewModel.saw()
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
        var likes: Int,
        val sharedByMe: Boolean,
        var shares: Int,
        val sawByMe: Boolean,
        var visibility: Int
    )
}






