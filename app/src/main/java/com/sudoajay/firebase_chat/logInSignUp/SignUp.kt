package com.sudoajay.firebase_chat.logInSignUp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sudoajay.firebase_chat.activity.BaseActivity
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.databinding.FragmentLoginBinding
import com.sudoajay.firebase_chat.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : Fragment() {

    private var isDarkTheme: Boolean = false
    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myDrawerView = layoutInflater.inflate(R.layout.fragment_signup, null)
        binding = FragmentSignupBinding.inflate(layoutInflater, myDrawerView as ViewGroup, false)


        return binding.root

    }
}
