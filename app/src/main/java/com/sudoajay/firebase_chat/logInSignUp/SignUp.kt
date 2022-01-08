package com.sudoajay.firebase_chat.logInSignUp

import android.os.Build
import android.os.Bundle
import androidx.core.view.WindowInsetsControllerCompat
import com.sudoajay.firebase_chat.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : BaseActivity() {

    private var isDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isDarkTheme = isSystemDefaultOn()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDarkTheme) {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    true
            }


        }
    }
}
