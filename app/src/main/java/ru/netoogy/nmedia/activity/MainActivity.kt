package ru.netoogy.nmedia.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import ru.netoogy.nmedia.R
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
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

        val newPostLauncher = registerForActivityResult(NewPostContract) {
            if (it == null) {
                viewModel.cancel()
            } else {
                viewModel.save(it)
            }
        }

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRepost(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val chooser =
                    Intent.createChooser(intent, getString(R.string.description_post_share))
                startActivity(chooser)
                viewModel.repostById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                newPostLauncher.launch(post.content)
            }

            override fun onPlayVideo(post: Post) {
                post.videoUrl?.let { url ->
                    try {
                        val videoId = post.videoUrl.split("/").getOrNull(4)

                        if (videoId != null) {
                            val intent = Intent(Intent.ACTION_VIEW, post.videoUrl.toUri())

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    } catch (e: ActivityNotFoundException) {
                       Toast.makeText(this@MainActivity, "Нет приложения для просмотра видео", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        binding.listPosts.adapter = adapter
        viewModel.data.observe(this) { posts -> adapter.submitList(posts) }

        binding.add.setOnClickListener {
            newPostLauncher.launch("Текст поста по умолчанию")
        }
    }
}