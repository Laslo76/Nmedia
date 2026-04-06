package ru.netoogy.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netoogy.nmedia.databinding.FragmentNewPostBinding
import ru.netoogy.nmedia.util.AndroidUtils
import ru.netoogy.nmedia.viewmodel.PostViewModel
import kotlin.getValue

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()
    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString().trim()
            if (text.isNotBlank()) {
                viewModel.save(text)
            }
            AndroidUtils.hideKeyboard(requireView())
             findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.edited.observe(viewLifecycleOwner) { post ->

            if (post?.id != 0) {
                binding.edit.setText(post?.content)
            }
            AndroidUtils.showKeyboard(binding.edit)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancel()
        _binding = null
    }
}