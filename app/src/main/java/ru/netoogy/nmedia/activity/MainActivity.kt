package ru.netoogy.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netoogy.nmedia.R
import ru.netoogy.nmedia.databinding.ActivityMainBinding
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.viewmodel.PostViewModel

class  MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                countViews.text = countToString(post.views)
                countHearts.text = countToString(post.likes)
                countReposts.text = countToString(post.shared)
                heart.setImageResource(
                    if (post.isLiked) R.drawable.ic_topic_heart_liked else R.drawable.ic_topic_heart
                )
            }
        }

        binding.heart.setOnClickListener { viewModel.like() }
        binding.reposts.setOnClickListener { viewModel.repost() }
    }

    fun countToString(value: Int): String {
        return when {
            value > 999_999 -> "${value / 1_000_000}.${(value % 1_000_000) / 100_000}M"
            value in 10_000..999_999 -> "${value / 1_000}K"
            value > 999 -> "${value / 1_000}.${(value % 1_000) / 100}K"
            else -> "$value"
        }
    }
}