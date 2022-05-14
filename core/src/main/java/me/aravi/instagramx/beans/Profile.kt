package me.aravi.instagramx.beans

import androidx.annotation.Keep

@Keep
data class Profile(
    val id: String = "",
    val bio: String? = "",
    val extUrl: String? = "",
    val fullName: String? = "",
    val username: String = "",
    val profilePic: String? = "",
    val profilePicHD: String? = "",

    val has_ar_effects: Boolean = false,
    val has_clips: Boolean = false,
    val has_guides: Boolean = false,
    val has_channel: Boolean = false,

    val is_business_account: Boolean = false,
    val is_professional_account: Boolean = false,
    val is_joined_recently: Boolean = false,
    val followed_by_viewer: Boolean = false,
    val requested_by_viewer: Boolean = false,

    val is_verified: Boolean = false,
    val is_private: Boolean = false,

    val followers: Long = 0L,
    val following: Long = 0L,

    val videoCount: Long = 0L,
    val postCount: Long = 0L,
    val highlight_reel_count: Long = 0L,
)