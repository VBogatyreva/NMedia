package ru.netology.nmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNewPost.requestFocus()

        binding.OkNewPost.setOnClickListener {
            val intent = Intent()
            if (binding.addNewPost.text.isNullOrBlank()) {
                Toast.makeText(
                    this@NewPostActivity,
                    "Content can`t be empty",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.addNewPost.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}






