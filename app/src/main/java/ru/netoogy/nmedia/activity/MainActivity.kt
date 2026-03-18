package ru.netoogy.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netoogy.nmedia.adapter.OnInteractionListener
import ru.netoogy.nmedia.adapter.PostAdapter
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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter( object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRepost( post: Post) {
                viewModel.repostById(post.id)
            }

            override fun onRemove( post: Post) {
                viewModel.removeById( post.id )
            }
        })

        binding.listPosts.adapter = adapter
        viewModel.data.observe(this) {posts -> adapter.submitList(posts)}
    }

}