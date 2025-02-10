package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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

        if (binding.addNewPost.text.toString().isEmpty()) {
            binding.addNewPost.setText(viewModel.getDraft())
        }

        binding.OkNewPost.setOnClickListener {

            viewModel.changeContent(binding.addNewPost.text.toString())
            viewModel.save()
            viewModel.dropDraft()
            AndroidUnils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.saveDraft(binding.addNewPost.text.toString())
            findNavController().navigateUp()
        }

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}






