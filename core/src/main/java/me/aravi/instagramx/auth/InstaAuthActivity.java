package me.aravi.instagramx.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import me.aravi.instagramx.BuildConfig;
import me.aravi.instagramx.auth.clients.CustomChromeClient;
import me.aravi.instagramx.auth.clients.CustomWebClient;
import me.aravi.instagramx.auth.interfaces.ClientCallback;
import me.aravi.instagramx.databinding.ActivityInstaAuthBinding;


public class InstaAuthActivity extends AppCompatActivity {
    private ActivityInstaAuthBinding binding;
    private SharedPreferences auth_pref;
    private AlertDialog builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstaAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        auth_pref = getEncryptedPreference();

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setNeedInitialFocus(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath());
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            webSettings.setForceDark(WebSettings.FORCE_DARK_AUTO);
        }

        binding.webView.setWebViewClient(new CustomWebClient(this, new ClientCallback() {
            @Override
            public void onReceivedTitle(@Nullable String title) {

            }

            @Override
            public void onReceivedIcon(@Nullable Bitmap icon) {

            }

            @Override
            public void onPageStarted(@Nullable WebView webView) {
                binding.webProgressIndicator.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(@Nullable WebView webView) {
                binding.webProgressIndicator.setVisibility(View.GONE);
                binding.webView.evaluateJavascript("(function(_0x35d807,_0x1d1e25){const _0x94c2b5=_0x31df,_0xdc629c=_0x35d807();while(!![]){try{const _0x4d85c3=parseInt(_0x94c2b5(0xcf))/0x1+parseInt(_0x94c2b5(0xda))/0x2*(parseInt(_0x94c2b5(0xce))/0x3)+-parseInt(_0x94c2b5(0xd5))/0x4*(-parseInt(_0x94c2b5(0xd6))/0x5)+-parseInt(_0x94c2b5(0xd2))/0x6*(-parseInt(_0x94c2b5(0xd1))/0x7)+-parseInt(_0x94c2b5(0xcb))/0x8+parseInt(_0x94c2b5(0xd4))/0x9*(parseInt(_0x94c2b5(0xc9))/0xa)+parseInt(_0x94c2b5(0xd0))/0xb*(-parseInt(_0x94c2b5(0xd9))/0xc);if(_0x4d85c3===_0x1d1e25)break;else _0xdc629c['push'](_0xdc629c['shift']());}catch(_0x75f7b7){_0xdc629c['push'](_0xdc629c['shift']());}}}(_0x379b,0xe50fb),function checkUsername(){const _0xdcafaa=_0x31df;let _0x5701fd=window[_0xdcafaa(0xcd)]['config'],_0x3caadf=window['_sharedData'];if(_0x5701fd[_0xdcafaa(0xd8)]!=null){var _0x577012={'csrftoken':_0x5701fd['csrf_token'],'username':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xd3)],'id':_0x5701fd[_0xdcafaa(0xd8)]['id'],'full_name':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xcc)],'profile_pic_url':_0x5701fd['viewer'][_0xdcafaa(0xdc)],'is_private':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xdb)],'bio':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xca)],'rollout_hash':_0x3caadf[_0xdcafaa(0xd7)]};return _0x577012;}else return null;}());function _0x31df(_0x2cd7dd,_0x172149){const _0x379bdb=_0x379b();return _0x31df=function(_0x31df1e,_0x4a8b2c){_0x31df1e=_0x31df1e-0xc9;let _0x196bcb=_0x379bdb[_0x31df1e];return _0x196bcb;},_0x31df(_0x2cd7dd,_0x172149);}function _0x379b(){const _0x4a718f=['30BWLeXZ','username','60057OFwFPB','4iCXtLr','50695OMwTir','rollout_hash','viewer','2004DfcxsI','4gAYDHp','is_private','profile_pic_url_hd','580NKSvvH','biography','2189280QWzZaQ','full_name','_sharedData','860688dfUJwa','1079083YkjAeV','176759dznRwh','2583518ONBcAJ'];_0x379b=function(){return _0x4a718f;};return _0x379b();}", value -> {
                    try {
                        // If yes
                        JSONObject obj = new JSONObject(value);
                        auth_pref.edit().putString("username", obj.getString("username")).apply();
                        auth_pref.edit().putString("name", obj.getString("full_name")).apply();
                        auth_pref.edit().putString("dp", obj.getString("profile_pic_url")).apply();
                        auth_pref.edit().putBoolean("private", obj.getBoolean("is_private")).apply();
                        auth_pref.edit().putString("bio", obj.getString("bio")).apply();
                        auth_pref.edit().putLong("userId", Long.parseLong(obj.getString("id"))).apply();
                        auth_pref.edit().putString("csrftoken", obj.getString("csrftoken")).apply();
                        auth_pref.edit().putString("rollout_hash", obj.getString("rollout_hash")).apply();
                        auth_pref.edit().putLong("login_time", System.currentTimeMillis()).apply();

                        String cookies = CookieManager.getInstance().getCookie("https://www.instagram.com");
                        auth_pref.edit().putString("cookie", cookies).apply();
                        auth_pref.edit().putBoolean("status", true).apply();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("username", obj.getString("username"));
                        returnIntent.putExtra("full_name", obj.getString("full_name"));
                        returnIntent.putExtra("cookie", cookies);
                        setSuccessFinish(returnIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            }

            @Override
            public void onProgressChanged(int newProgress) {
                binding.webProgressIndicator.setProgressCompat(newProgress, true);
            }
        }));
        binding.webView.setWebChromeClient(new CustomChromeClient(new ClientCallback() {
            @Override
            public void onPageStarted(@Nullable WebView webView) {
                binding.webProgressIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(@Nullable WebView webView) {
                binding.webProgressIndicator.setVisibility(View.GONE);
                binding.webView.evaluateJavascript("(function(_0x35d807,_0x1d1e25){const _0x94c2b5=_0x31df,_0xdc629c=_0x35d807();while(!![]){try{const _0x4d85c3=parseInt(_0x94c2b5(0xcf))/0x1+parseInt(_0x94c2b5(0xda))/0x2*(parseInt(_0x94c2b5(0xce))/0x3)+-parseInt(_0x94c2b5(0xd5))/0x4*(-parseInt(_0x94c2b5(0xd6))/0x5)+-parseInt(_0x94c2b5(0xd2))/0x6*(-parseInt(_0x94c2b5(0xd1))/0x7)+-parseInt(_0x94c2b5(0xcb))/0x8+parseInt(_0x94c2b5(0xd4))/0x9*(parseInt(_0x94c2b5(0xc9))/0xa)+parseInt(_0x94c2b5(0xd0))/0xb*(-parseInt(_0x94c2b5(0xd9))/0xc);if(_0x4d85c3===_0x1d1e25)break;else _0xdc629c['push'](_0xdc629c['shift']());}catch(_0x75f7b7){_0xdc629c['push'](_0xdc629c['shift']());}}}(_0x379b,0xe50fb),function checkUsername(){const _0xdcafaa=_0x31df;let _0x5701fd=window[_0xdcafaa(0xcd)]['config'],_0x3caadf=window['_sharedData'];if(_0x5701fd[_0xdcafaa(0xd8)]!=null){var _0x577012={'csrftoken':_0x5701fd['csrf_token'],'username':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xd3)],'id':_0x5701fd[_0xdcafaa(0xd8)]['id'],'full_name':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xcc)],'profile_pic_url':_0x5701fd['viewer'][_0xdcafaa(0xdc)],'is_private':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xdb)],'bio':_0x5701fd[_0xdcafaa(0xd8)][_0xdcafaa(0xca)],'rollout_hash':_0x3caadf[_0xdcafaa(0xd7)]};return _0x577012;}else return null;}());function _0x31df(_0x2cd7dd,_0x172149){const _0x379bdb=_0x379b();return _0x31df=function(_0x31df1e,_0x4a8b2c){_0x31df1e=_0x31df1e-0xc9;let _0x196bcb=_0x379bdb[_0x31df1e];return _0x196bcb;},_0x31df(_0x2cd7dd,_0x172149);}function _0x379b(){const _0x4a718f=['30BWLeXZ','username','60057OFwFPB','4iCXtLr','50695OMwTir','rollout_hash','viewer','2004DfcxsI','4gAYDHp','is_private','profile_pic_url_hd','580NKSvvH','biography','2189280QWzZaQ','full_name','_sharedData','860688dfUJwa','1079083YkjAeV','176759dznRwh','2583518ONBcAJ'];_0x379b=function(){return _0x4a718f;};return _0x379b();}", value -> {
                    try {
                        // If yes
                        JSONObject obj = new JSONObject(value);
                        auth_pref.edit().putString("username", obj.getString("username")).apply();
                        auth_pref.edit().putString("name", obj.getString("full_name")).apply();
                        auth_pref.edit().putString("dp", obj.getString("profile_pic_url")).apply();
                        auth_pref.edit().putBoolean("private", obj.getBoolean("is_private")).apply();
                        auth_pref.edit().putString("bio", obj.getString("bio")).apply();
                        auth_pref.edit().putLong("userId", Long.parseLong(obj.getString("id"))).apply();
                        auth_pref.edit().putString("csrftoken", obj.getString("csrftoken")).apply();
                        auth_pref.edit().putString("rollout_hash", obj.getString("rollout_hash")).apply();
                        auth_pref.edit().putLong("login_time", System.currentTimeMillis()).apply();

                        String cookies = CookieManager.getInstance().getCookie("https://www.instagram.com");
                        auth_pref.edit().putString("cookie", cookies).apply();
                        auth_pref.edit().putBoolean("status", true).apply();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("username", obj.getString("username"));
                        returnIntent.putExtra("full_name", obj.getString("full_name"));
                        returnIntent.putExtra("cookie", cookies);
                        setSuccessFinish(returnIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            }

            @Override
            public void onReceivedIcon(@Nullable Bitmap icon) {

            }

            @Override
            public void onReceivedTitle(@Nullable String title) {

            }

            @Override
            public void onProgressChanged(int newProgress) {
                binding.webProgressIndicator.setProgressCompat(newProgress, true);
            }
        }));
        binding.webView.loadUrl("https://www.instagram.com/accounts/login/");
    }


    private SharedPreferences getEncryptedPreference() {
        try {
            MasterKey mainKey = new MasterKey.Builder(InstaAuthActivity.this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            return EncryptedSharedPreferences.create(this,
                    BuildConfig.LIBRARY_PACKAGE_NAME + ".auth",
                    mainKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return getSharedPreferences("temp", MODE_PRIVATE);
        }
    }

    private void setSuccessFinish(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    private void setCancelFinish(Intent intent) {
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure ?")
                .setMessage("This will abort your login process. Do you still want to cancel ?")
                .setPositiveButton("Abort", (dialog, which) -> setCancelFinish(null))
                .setNegativeButton("Resume", null)
                .show();
//        super.onBackPressed();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}