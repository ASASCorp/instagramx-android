package me.aravi.instagramx.di

import android.content.Context
import me.aravi.instagramx.BuildConfig
import me.aravi.instagramx.InstaConfig
import me.aravi.instagramx.InstagramX
import me.aravi.instagramx.api.InstagramApi
import me.aravi.instagramx.utils.NetworkUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val instaNetworkModule = module {
    single { createOkHttpClient(androidContext()) }
    factory { createApi<InstagramApi>(get(), InstaConfig.BASE_URL) }
}

val instaAppModule = module {
    single { InstagramX(get()) }
}

val instagramX = listOf(instaAppModule, instaNetworkModule)

private fun interceptor(context: Context): Interceptor =
    Interceptor { chain ->
        val originalResponse: Response = chain.proceed(chain.request())
        val age = if (BuildConfig.DEBUG) 1 else 60

        val cacheControl = CacheControl.Builder()
            .maxAge(age, TimeUnit.MINUTES)
            .build()

        if (NetworkUtils.isNetworkAvailable(context)) {
            originalResponse.newBuilder()
                .removeHeader("Cache-Control")
                .removeHeader("Pragma")
                .header("Cache-Control", cacheControl.toString())
                .build()
        } else {
            val maxStale = 60 * 60 * 24 // 1 day
            originalResponse.newBuilder()
                .removeHeader("Cache-Control")
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }

    }

fun createOkHttpClient(context: Context): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

    val httpCacheDirectory =
        File(context.cacheDir, "${BuildConfig.LIBRARY_PACKAGE_NAME}_network_cache")
    val cacheSize = 50 * 1024 * 1024 // 50 MB
    val cache = Cache(httpCacheDirectory, cacheSize.toLong())

    return OkHttpClient.Builder()
        .cache(cache)
        .addNetworkInterceptor(interceptor(context))
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

inline fun <reified T> createApi(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}

