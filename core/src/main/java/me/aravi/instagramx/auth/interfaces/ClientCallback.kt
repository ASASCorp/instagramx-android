package me.aravi.instagramx.auth.interfaces

import android.graphics.Bitmap
import android.webkit.WebView

interface ClientCallback {

    fun onPageStarted(webView: WebView?) {

    }

    fun onPageFinished(webView: WebView?) {

    }


    fun onReceivedIcon(icon: Bitmap?) {

    }

    fun onReceivedTitle(title: String?) {

    }

    fun onProgressChanged(newProgress: Int) {

    }
}