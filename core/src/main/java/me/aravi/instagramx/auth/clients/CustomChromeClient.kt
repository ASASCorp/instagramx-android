package me.aravi.instagramx.auth.clients

import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import me.aravi.instagramx.auth.interfaces.ClientCallback

class CustomChromeClient(val callback: ClientCallback) : WebChromeClient() {

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        super.onReceivedIcon(view, icon)
        callback.onReceivedIcon(icon)
    }


    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        callback.onReceivedTitle(title)
    }


    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        callback.onProgressChanged(newProgress)
    }
}