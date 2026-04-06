package ru.netoogy.nmedia.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netoogy.nmedia.R
import ru.netoogy.nmedia.databinding.FragmentPostBinding
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.viewmodel.PostViewModel
import kotlin.getValue

class PostFragment : Fragment()  {
    private val viewModel: PostViewModel by activityViewModels()
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!


    fun onRepost(post: Post) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, post.content)
        }
        val chooser = Intent.createChooser(intent, getString(R.string.description_post_share))
        startActivity(chooser)
        viewModel.repostById(post.id)
    }

    fun onRemove(post: Post) {
        viewModel.removeById(post.id)
        findNavController().navigateUp()
    }

    fun onEdit(post: Post) {
        viewModel.edit(post)
        findNavController().navigate(R.id.action_postFragment_to_newPostFragment)
    }
    fun onLikePost(post: Post) {
        viewModel.likeById(post.id)
    }
    fun onPlayVideo(post: Post) {
        post.videoUrl?.let { url ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                startActivity(intent)

            } catch (_: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "Нет приложения для просмотра видео",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = viewModel.getById(viewModel.viewRecordID)
            if (post != null) {
                binding.apply {
                    author.text = post.author
                    published.text = post.published
                    content.text = post.content
                    countViews.text = "${post.views}"

                    reposts.text = "${post.shared}"
                    heart.isChecked = post.isLiked
                    heart.text = "${post.likes}"

                    video.visibility =
                        if (post.videoUrl.isNullOrEmpty()) View.GONE else View.VISIBLE
                    video.setOnClickListener {
                        onPlayVideo(post)
                    }

                    heart.setOnClickListener {
                        onLikePost(post)
                    }

                    reposts.setOnClickListener {
                        onRepost(post)
                    }

                    menu.setOnClickListener {
                        PopupMenu(it.context, it).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.remove -> {
                                        onRemove(post)
                                        true
                                    }

                                    R.id.edit -> {
                                        onEdit(post)
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }.show()
                    }
                }
            } else findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
            _binding = null
    }
}