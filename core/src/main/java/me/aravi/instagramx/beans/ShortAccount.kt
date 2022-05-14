/*
 * Copyright (c) 2022. Aravind Chowdary
 */

package me.aravi.instagramx.beans

import androidx.annotation.Keep

@Keep
data class ShortAccount(
    val id: String = "",
    val username: String = "",
    val full_name: String = "",
    val profile_pic_url: String = "",
    val is_private: Boolean = false,
    val is_verified: Boolean = false,
    val followed_by_viewer: Boolean = false,
    val requested_by_viewer: Boolean = false,
)