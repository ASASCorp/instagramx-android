package me.aravi.instagramx.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.aravi.instagramx.InstagramX
import me.aravi.instagramx.api.InstagramApi
import me.aravi.instagramx.auth.InstaAuth
import me.aravi.instagramx.auth.InstaUser
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    val instagramApi: InstagramApi by inject()
    private lateinit var instagramAuth: InstaAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instagramAuth = InstaAuth.getInstance(this)

        // init
        val instagramX = InstagramX(instagramApi)

    }

    fun login() {
        startActivity(instagramAuth.startAuth())
    }


    fun user(): InstaUser {
        return instagramAuth.currentUser
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}