package com.sudoajay.firebase_chat.logInSignUp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.activity.BaseActivity
import com.sudoajay.firebase_chat.databinding.FragmentSignupBinding
import com.sudoajay.firebase_chat.helper.Toaster
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : Fragment() {

    private var isDarkTheme: Boolean = false
    lateinit var binding: FragmentSignupBinding
    private lateinit var mAuth: FirebaseAuth
    private var TAG = "SignUpTAG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myDrawerView = layoutInflater.inflate(R.layout.fragment_signup, null)
        binding = FragmentSignupBinding.inflate(layoutInflater, myDrawerView as ViewGroup, false)
        binding.fragment = this
        binding.lifecycleOwner = this

        reference()

        return binding.root

    }


    private fun reference() {
        isDarkTheme = BaseActivity.isSystemDefaultOn(resources)
        binding.headingImageView.setImageResource(if (isDarkTheme) R.drawable.signup_night else R.drawable.signup)
        mAuth = FirebaseAuth.getInstance()

        binding.firstNameTextInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank())
                binding.firstNameTextInputLayout.isErrorEnabled = false
        }
        binding.lastNameTextInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank())
                binding.lastNameTextInputLayout.isErrorEnabled = false
        }

        binding.emailOrPhoneTextInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank())
                binding.emailOrPhoneTextInputLayout.isErrorEnabled = false
        }
        binding.passwordTextInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank())
                binding.passwordTextInputLayout.isErrorEnabled = false
        }

    }

    fun clickSignUpButton() {
        if (!isCheckForEmptyText()) {
            val emailOrPhone = binding.emailOrPhoneTextInputLayoutEditText.text.toString()
            val pass = binding.passwordTextInputLayoutEditText.text.toString()
            signUp(emailOrPhone, pass)
        }
    }

    private fun isCheckForEmptyText(): Boolean {
        var value = ""
        var isEmpty = true
        when {
            binding.firstNameTextInputLayoutEditText.text.isNullOrBlank() -> {
                value = getString(R.string.somethingEmpty_text)
                binding.firstNameTextInputLayout.error = value
            }
            binding.lastNameTextInputLayoutEditText.text.isNullOrBlank() -> {
                value = getString(R.string.somethingEmpty_text)
                binding.lastNameTextInputLayout.error = value
            }
            binding.emailOrPhoneTextInputLayoutEditText.text.isNullOrBlank() -> {
                value = getString(R.string.emailOrPhoneEmpty_text)
                binding.emailOrPhoneTextInputLayout.error = value
            }
            binding.passwordTextInputLayoutEditText.text.isNullOrBlank() -> {
                value = getString(R.string.passwordEmpty_text)
                binding.passwordTextInputLayout.error = value
            }
            else -> isEmpty = false
        }
        throwToaster(value)
        return isEmpty
    }

    private fun throwToaster(value: String) {
        Toaster.showToast(requireContext(), value)
    }


    private fun signUp(email: String, pass: String) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.i(TAG, "createUserWithEmail:success")
                val user = mAuth.currentUser

            } else {
                // If sign in fails, display a message to the user.
                Log.e(TAG, "createUserWithEmail:failure ${it.exception}")
                throwToaster(getString(R.string.errorSignUp_text))
            }
        }
    }
}
