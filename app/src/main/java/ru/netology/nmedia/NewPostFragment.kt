package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import java.io.File
import java.io.FileOutputStream

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    private var fragmentBinding: FragmentNewPostBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        arguments?.textArg?.let {
            binding.addNewPost.setText(it)
        }

        fragmentBinding = binding

        binding.addNewPost.requestFocus()

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadVisiblePosts()
            findNavController().navigateUp()
        }

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri = it.data?.data ?: return@registerForActivityResult
                        val file = uri.toFile(requireContext())
                        viewModel.changePhoto(uri, file)
                    }
                }
            }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.removePhoto.setOnClickListener {
            viewModel.changePhoto(null, null)
        }

        viewModel.photo.observe(viewLifecycleOwner) { photo ->
            if (photo == null) {
                binding.photoContainer.isGone = true
                return@observe
            }

            binding.photoContainer.isVisible = true
            binding.photo.setImageURI(photo.uri)
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_post, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.save -> {
                        val text = binding.addNewPost.text.toString()
                        viewModel.changeContent(text)
                        viewModel.save()
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
    private fun Uri.toFile(context: Context): File? {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)
        return try {
            contentResolver.openInputStream(this)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}






