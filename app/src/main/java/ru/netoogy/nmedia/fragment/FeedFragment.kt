package ru.netoogy.nmedia.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netoogy.nmedia.R
import ru.netoogy.nmedia.adapter.OnInteractionListener
import ru.netoogy.nmedia.adapter.PostAdapter
import ru.netoogy.nmedia.databinding.FragmentFeedBinding
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.viewmodel.PostViewModel


class  FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by activityViewModels()

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onPostClick(post: Post) {
                viewModel.view(post)
                findNavController().navigate(R.id.action_FeedFragment_to_postFragment)

            }

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
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            }

            override fun onPlayVideo(post: Post) {
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
        })

        binding.listPosts.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts -> adapter.submitList(posts) }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }
}