package ru.netoogy.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netoogy.nmedia.R
import ru.netoogy.nmedia.databinding.ActivityNewPostBinding
import ru.netoogy.nmedia.dto.Post

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialText = intent.getStringExtra(NewPostContract.KEY_TEXT)
        if (!initialText.isNullOrEmpty()) {
            binding.edit.setText(initialText)
            // Ставим курсор в конец текста для удобства
            binding.edit.setSelection(initialText.length)
        }

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val intent = Intent().putExtra(NewPostContract.KEY_TEXT, text)
                setResult(RESULT_OK, intent)
            }
            finish()
        }
    }
}

object NewPostContract: ActivityResultContract<String?, String?>(){
    const val KEY_TEXT = "post_text"

    override fun createIntent(context: Context, input: String?): Intent {
        val intent = Intent(context, NewPostActivity::class.java)
        // Передаем строку через extra. Если input == null, передаем пустую строку или null.
        input?.let { intent.putExtra(KEY_TEXT, it)}
        return intent
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ) = intent?.getStringExtra(KEY_TEXT)

}

