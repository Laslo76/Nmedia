package ru.netoogy.nmedia

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netoogy.nmedia.databinding.ActivityMainBinding
import ru.netoogy.nmedia.dto.Post

class  MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom)
            insets
        }

        val post = Post(
            1,
            "Нетология. Университет интернет-профессий будущего",
            "21 мая в 18:36",
            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            9998,
            false,
            10501,
            12101000
        )
        with (binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countViews.text = countToString(post.views)
            countHearts.text = countToString(post.likes)
            countReposts.text = countToString(post.shared)
            if (post.isLiked) {
                heart.setImageResource(R.drawable.ic_topic_heart_liked)
            }
            heart.setOnClickListener{
                if (post.isLiked) post.likes-- else post.likes++
                post.isLiked = !post.isLiked
                heart.setImageResource(if (post.isLiked) R.drawable.ic_topic_heart_liked else R.drawable.ic_topic_heart)
                countHearts.text = countToString(post.likes)
            }

            reposts.setOnClickListener{
                post.shared++
                countReposts.text = countToString(post.shared)
            }

        }
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