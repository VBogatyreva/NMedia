package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(inflater, container,false)

        binding.addNewPost.requestFocus()

        arguments?.textArg?.let(binding.addNewPost::setText)


        binding.OkNewPost.setOnClickListener {

            viewModel.changeContent(binding.addNewPost.text.toString())
            viewModel.save()
            AndroidUnils.hideKeyboard(requireView())
            findNavController().navigateUp()

        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}






