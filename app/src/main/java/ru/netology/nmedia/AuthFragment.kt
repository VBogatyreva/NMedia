package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.databinding.FragmentAuthBinding

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            val login = binding.loginEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (login.isBlank() || password.isBlank()) {
                Snackbar.make(
                    binding.root,
                    "Логин и пароль не могут быть пустыми",
                    Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            viewModel.authenticate(login, password)
        }

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.signInButton.isEnabled = false
                }
                is AuthState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.signInButton.isEnabled = true
                    findNavController().popBackStack()
                }
                is AuthState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.signInButton.isEnabled = true
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    sealed class AuthState {
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }
}