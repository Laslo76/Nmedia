package ru.netoogy.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import ru.netoogy.nmedia.R
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netoogy.nmedia.adapter.OnInteractionListener
import ru.netoogy.nmedia.adapter.PostAdapter
import ru.netoogy.nmedia.databinding.ActivityMainBinding
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.util.AndroidUtils
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
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        })

        val editGroup: ConstraintLayout = findViewById(R.id.editGroup)

        binding.listPosts.adapter = adapter
        viewModel.data.observe(this) {posts -> adapter.submitList(posts)}

        viewModel.edited.observe(this) { post ->

            if (post.id != 0) {
                with(binding.content) {

                    setText(post.content)
                    AndroidUtils.showKeyboard(this)
                }
            }
        }

        binding.save.setOnClickListener {

            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.save(text.toString())

                setText("")
                clearFocus()
                editGroup.visibility = View.GONE

                AndroidUtils.hideKeyboard(this)
            }
        }

        binding.cancel.setOnClickListener {
            with(binding.content) {
                setText("")
                clearFocus()
                editGroup.visibility = View.GONE

                AndroidUtils.hideKeyboard(this)
            }
            viewModel.cancel()
        }

        binding.content.setOnFocusChangeListener {view, hasFocus ->
            if (hasFocus) {
                // Если EditText получил фокус, показываем группу
                editGroup.visibility = View.VISIBLE
            }
        }
    }

}