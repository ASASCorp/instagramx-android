package me.aravi.instagramx.beans

import androidx.annotation.Keep

@Keep
data class Post(
    val id: String = "",
    val typeName: String = "",
    val displayUrl: String = "",
    val captionText: String? = "",
    val shortCode: String = "",
    val thumbnail: String? = "",
    val videoUrl: String? = "",

    val resources: Map<String, String>? = mapOf(), // map of quality and src url
    val location: Map<String, String>? = mapOf(),

    val isVideo: Boolean = false,
    val commentsDisabled: Boolean = false,
    val likedByUser: Boolean = false,
    val savedByUser: Boolean = false,

    val type: Int = 0, // type Image, Video, GraphSlideCar
    val takenAtTimestamp: Long = 0,
    val commentCount: Long = 0L,
    val likeCount: Long = 0L,
)