package me.aravi.instagramx.auth.clients

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import me.aravi.instagramx.auth.interfaces.ClientCallback


class CustomWebClient(val context: Context, val callback: ClientCallback) : WebViewClient() {
    private val HTTP = "http://"
    private val HTTPS = "https://"


    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url == null) {
            return true
        }

        view!!.loadUrl(url)
        return true
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        Log.i("WebClient", "shouldInterceptRequest: " + request.toString())
        return super.shouldInterceptRequest(view, request)
    }


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        callback.onPageStarted(view)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        callback.onPageFinished(view)
    }
}