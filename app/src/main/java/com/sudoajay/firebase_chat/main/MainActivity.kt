package com.sudoajay.firebase_chat.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.sudoajay.firebase_chat.BaseActivity
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    var TAG = "MainActivityTAG"

    lateinit var binding: ActivityMainBinding

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }
}