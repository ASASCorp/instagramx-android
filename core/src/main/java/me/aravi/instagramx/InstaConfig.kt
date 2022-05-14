package me.aravi.instagramx

object InstaConfig {
    var BASE_URL = "https://www.instagram.com/"

    const val USER_AGENT =
        "Mozilla/5.0 (Linux; Android 10; SM-G973F Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/86.0.4240.198 Mobile Safari/537.36 Instagram 166.1.0.42.245 Android (29/10; 420dpi; 1080x2042; samsung; SM-G973F; beyond1; exynos9820; en_GB; 256099204)"

    var QUERY_HASH_POSTS = "f2405b236d85e8296cf30347c9f08c2a"
    var QUERY_HASH_FOLLOWING = "d04b0a864b4b54837c0d870b0e77e076"
    var QUERY_HASH_FOLLOWERS = "c76146de99bb02f6415203be841dd25a"

    const val SERVER_ERROR_CODE = 293
    const val CONNECTION_ERROR_CODE = 243
    const val PARSE_ERROR_CODE = 233
    const val UNKNOWN_ERROR_CODE = 223
}