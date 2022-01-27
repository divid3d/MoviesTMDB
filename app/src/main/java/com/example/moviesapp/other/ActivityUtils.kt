package com.example.moviesapp.other

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

open class CaptureSpeechToText : ActivityResultContract<Void?, String?>() {
    @CallSuper
    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
    }

    final override fun getSynchronousResult(
        context: Context,
        input: Void?
    ): SynchronousResult<String?>? = null

    @Suppress("AutoBoxing")
    final override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode == RESULT_OK) {
            val results = intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            return results?.firstOrNull()
        }

        return null
    }

}