package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import kotlinx.android.synthetic.main.netology_main.author
//import kotlinx.android.synthetic.main.netology_main.content
//import kotlinx.android.synthetic.main.netology_main.countLikes
//import kotlinx.android.synthetic.main.netology_main.countSharing
import ru.netology.nmedia.databinding.ActivityMainBinding

//import kotlinx.android.synthetic.main.netology_main.likes
//import kotlinx.android.synthetic.main.netology_main.published
//import kotlinx.android.synthetic.main.netology_main.share
import ru.netology.nmedia.databinding.NetologyMainBinding

class MainActivity : AppCompatActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = NetologyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интренет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            likedByMe = false,
            likes = 0,
            sharedByMe = false,
            shares = 0
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content


            if (post.likedByMe) {
                likes?.setImageResource(R.drawable.ic_launcher_liked_foreground)
            }
            countLikes?.text = post.likes.toString()

            likes?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_launcher_liked_foreground else R.drawable.ic_launcher_like_foreground
                )
                if (post.likedByMe) post.likes++ else post.likes--
                countLikes?.text = numberFormat(post.likes)


            }

            if (post.sharedByMe) {
                share?.setImageResource(R.drawable.ic_launcher_share_foreground)
            }
            countSharing?.text = post.shares.toString()

            share?.setOnClickListener {
                post.sharedByMe = !post.sharedByMe
                post.shares++
                countSharing?.text = numberFormat(post.shares)
            }

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
        var likedByMe: Boolean,
        var likes: Int,
        var sharedByMe: Boolean,
        var shares: Int
    )
}


