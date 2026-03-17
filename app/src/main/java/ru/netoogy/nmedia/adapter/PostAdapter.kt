package ru.netoogy.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netoogy.nmedia.R
import ru.netoogy.nmedia.databinding.CardPostBinding
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.opportunities.countToString

typealias OnLikeListener = (post: Post) -> Unit
typealias OnRepostListener = (post: Post) -> Unit


class PostAdapter (
    private val onLikeListener: OnLikeListener,
    private val onRepostListener: OnRepostListener
): ListAdapter<Post, PostViewHolder>(PostDiffCallback)
{

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onRepostListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onRepostListener: OnRepostListener): RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countViews.text = countToString(post.views)
            countHearts.text = countToString(post.likes)
            countReposts.text = countToString(post.shared)
            heart.setImageResource(
                if (post.isLiked) R.drawable.ic_topic_heart_liked else R.drawable.ic_topic_heart
            )
            heart.setOnClickListener { onLikeListener(post) }
            reposts.setOnClickListener { onRepostListener(post) }
        }
    }
}

object PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}