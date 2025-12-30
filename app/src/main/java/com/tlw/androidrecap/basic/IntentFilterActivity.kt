package com.tlw.androidrecap.basic

import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tlw.androidrecap.R
import com.tlw.androidrecap.databinding.ActivityIntentFilterBinding
import kotlin.jvm.java

class IntentFilterActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntentFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIntentFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleSharedImage(intent)
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        handleSharedImage(intent)
    }

    private fun handleSharedImage(intent: Intent?) {
        Log.d("DEBUG_INTENT", "handleSharedImage called")

        if (intent == null) {
            Log.e("DEBUG_INTENT", "Intent is NULL")
            return
        }

        Log.d("DEBUG_INTENT", "Action: ${intent.action}")
        Log.d("DEBUG_INTENT", "Type: ${intent.type}")

        if (intent.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true) {

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
            }

            Log.d("DEBUG_INTENT", "URI received: $uri")

            if (uri != null) {
                Log.d("DEBUG_INTENT", "Loading image into ImageView")

                Glide.with(this)
                    .load(uri)
                    .into(binding.imgImage)

            } else {
                Log.e("DEBUG_INTENT", "URI is NULL — EXTRA_STREAM is missing!")
            }

        } else {
            Log.e("DEBUG_INTENT", "Intent action or type did NOT match ACTION_SEND or image/*")
        }
    }

}