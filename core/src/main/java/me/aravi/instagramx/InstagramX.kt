package me.aravi.instagramx

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat
import me.aravi.instagramx.api.InstagramApi
import me.aravi.instagramx.auth.InstaUser
import me.aravi.instagramx.beans.Post
import me.aravi.instagramx.beans.Profile
import me.aravi.instagramx.beans.ShortAccount
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InstagramX(val instagramApi: InstagramApi) {

    companion object {
        interface OnLoaded {
            fun success(obj: Any?, raw: String?)
            fun failed(message: String?, log: String?)
        }

        data class PostResponse(
            val posts: List<Post>,
            val nextPage: Map<String, String> = mapOf(),
            val count: Long = 0
        )

        data class AccountResponse(
            val accounts: List<ShortAccount>,
            val nextPage: Map<String, String> = mapOf(),
            val count: Long = 0
        )
    }

    fun profile(user: InstaUser, username: String? = null, listener: OnLoaded) {
        val rUsername = username ?: user.username
        val profileCall =
            instagramApi.profile(rUsername, user.cookie, user.csrfToken!!, user.roll_hash)

        CoroutineScope(Dispatchers.IO).launch {
            profileCall.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val jsonRaw = response.body()
                        logcat { "RAW : $jsonRaw" }
                        val profile = parseProfileFrom(jsonRaw)
                        listener.success(profile, jsonRaw)
                    } else {
                        listener.failed(response.message(), " ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    logcat { t.asLog() }
                    listener.failed(t.message.toString(), t.asLog())
                }

            })
        }
    }


    fun posts(user: InstaUser, userId: String = user.userId.toString(), listener: OnLoaded) {
        val postCall =
            instagramApi.posts(
                cookie = user.cookie,
                csrfToken = user.csrfToken!!,
                roll_hash = user.roll_hash,
                variables = "{\"id\":\"${userId}\",\"first\":9}"
            )
        postCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val jsonRaw = response.body()
                    logcat { "RAW : $jsonRaw" }
                    val posts = parsePostsFrom(jsonRaw!!)
                    listener.success(posts, jsonRaw)
                } else {
                    listener.failed(response.message(), " ${response.body()}")
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                logcat { t.asLog() }
                listener.failed(t.message.toString(), t.asLog())
            }

        })


    }


    fun followers(user: InstaUser, after: String? = null, listener: OnLoaded) {
        val postCall = if (after == null) {
            logcat { "Query Initial with no next token " }
            instagramApi.followers(
                cookie = user.cookie,
                csrfToken = user.csrfToken!!,
                roll_hash = user.roll_hash,
                variables = "{\"id\":\"${user.userId}\",\"first\":50}"
            )
        } else {
            logcat { "Query with next token: $after" }
            instagramApi.followers(
                cookie = user.cookie,
                csrfToken = user.csrfToken!!,
                roll_hash = user.roll_hash,
                variables = "{\"id\":\"${user.userId}\",\"first\":50, \"after\":\"$after\"}"
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            postCall.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val jsonRaw = response.body()
                        logcat { "RAW : $jsonRaw" }
                        val followers = jsonRaw?.let { parseFollowers(it) }
                        listener.success(followers, jsonRaw)
                    } else {
                        logcat { "Failed respose : onResponse Method else statement" }
                        listener.failed(response.message(), " ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    logcat { "Onresponse : ${t.asLog()}" }
                    listener.failed(t.message.toString(), t.asLog())
                }

            })
        }

    }


    fun followees(user: InstaUser, after: String? = null, listener: OnLoaded) {
        val postCall = if (after == null) {
            instagramApi.followee(
                cookie = user.cookie,
                csrfToken = user.csrfToken!!,
                roll_hash = user.roll_hash,
                variables = "{\"id\":\"${user.userId}\",\"first\":50}"
            )
        } else {
            instagramApi.followee(
                cookie = user.cookie,
                csrfToken = user.csrfToken!!,
                roll_hash = user.roll_hash,
                variables = "{\"id\":\"${user.userId}\",\"first\":50,\"after\":\"$after\"}"
            )
        }

        postCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val jsonRaw = response.body()
                    logcat { "RAW : $jsonRaw" }
                    val followers = jsonRaw?.let { parseFollowees(it) }
                    listener.success(followers, jsonRaw)
                } else {
                    listener.failed(response.message(), " ${response.body()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                logcat { t.asLog() }
                listener.failed(t.message.toString(), t.asLog())
            }

        })
    }


    fun post(postId: String, user: InstaUser, listener: OnLoaded) {
        val postRequest = instagramApi.post(
            postId,
            cookie = user.cookie,
            csrfToken = user.csrfToken!!,
            roll_hash = user.roll_hash
        )
        CoroutineScope(Dispatchers.IO).launch {
            postRequest.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val raw = response.body()!!
                        listener.success(parsePost(raw), raw)

                    } else {
                        listener.failed("Error occured", response.raw().toString())
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    listener.failed("Network failure :", t.asLog())
                }

            })
        }
    }


    fun likePost(postId: String, user: InstaUser, listener: OnLoaded) {
        val likeRequest =
            instagramApi.like(
                cookie = user.cookie,
                csrfToken = user.csrfToken!!,
                roll_hash = user.roll_hash,
                postId = postId
            )
        CoroutineScope(Dispatchers.IO).launch {
            likeRequest.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    listener.success(response.message(), response.body())
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    logcat { t.asLog() }
                    listener.failed(t.message, t.asLog())
                }

            })
        }
    }


    fun followUser(userId: String, user: InstaUser, listener: OnLoaded) {
        val followRequest =
            instagramApi.follow(
                cookie = user.cookie,
                csrfToken = user.csrfToken!!,
                roll_hash = user.roll_hash,
                userId = userId
            )
        CoroutineScope(Dispatchers.IO).launch {
            followRequest.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    listener.success(response.message(), response.body())
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    logcat { t.asLog() }
                    listener.failed(t.message, t.asLog())
                }

            })
        }
    }


    // parsers

    fun parsePost(raw: String): Post? {
        try {
            val response = JSONObject(raw)
            val graphql = response.getJSONArray("items")
            val media = graphql[0] as JSONObject


            var imageVersions: JSONObject = if (media.has("carousel_media")) {
                val carousel = media.getJSONArray("carousel_media")[0] as JSONObject
                carousel.getJSONObject("image_versions2")
            } else {
                media.getJSONObject("image_versions2")
            }

            val iv_candidates = imageVersions.getJSONArray("candidates")
            val display_url = (iv_candidates[0] as JSONObject).getString("url")

            var video_url = ""
            if (media.has("video_versions")) {
                val videoVersions = media.getJSONArray("video_versions")
                video_url = (videoVersions[0] as JSONObject).getString("url")
            }

            val is_video = media.getBoolean("is_unified_video")
            val liked_by_user = media.getBoolean("has_liked")
            val thumbnail_url = display_url

            val saved_by_user = if (media.has("has_viewer_saved")) {
                media.getBoolean("has_viewer_saved")
            } else {
                false
            }

            val tempPost = Post(
                id = media.getString("id"),
                shortCode = media.getString("code"),
                displayUrl = display_url,
                isVideo = is_video,
                thumbnail = thumbnail_url,
                videoUrl = video_url,
                savedByUser = saved_by_user,
                likedByUser = liked_by_user,
            )
            return tempPost

        } catch (e: JSONException) {
            logcat { e.asLog() }
            return null
        }

    }


    fun parseFollowers(raw: String): AccountResponse {
        val accounts = mutableListOf<ShortAccount>()
        try {
            val response = JSONObject(raw)
            val data = response.getJSONObject("data")
            val user = data.getJSONObject("user")
            val timeline = user.getJSONObject("edge_followed_by")

            val count = timeline.getInt("count")
            val nextPage = mutableMapOf<String, String>()
            try {
                val pageInfo = timeline.getJSONObject("page_info")
                nextPage["has_next"] = pageInfo.getBoolean("has_next_page").toString()
                nextPage["end_cursor"] = pageInfo.getString("end_cursor")
            } catch (e: Exception) {
                logcat { e.asLog() }
            }


            val edges = timeline.getJSONArray("edges")

            for (i in 0 until edges.length()) {
                try {
                    val edge: JSONObject = edges.getJSONObject(i)
                    val node: JSONObject = edge.getJSONObject("node")

                    val shortAcc = ShortAccount(
                        id = node.getString("id"),
                        username = node.getString("username"),
                        full_name = node.getString("full_name"),
                        profile_pic_url = node.getString("profile_pic_url"),
                        is_private = node.getBoolean("is_private"),
                        is_verified = node.getBoolean("is_verified"),
                        followed_by_viewer = node.getBoolean("followed_by_viewer"),
                        requested_by_viewer = node.getBoolean("requested_by_viewer"),
                    )
                    accounts.add(shortAcc)

                } catch (e: java.lang.Exception) {
                    logcat { e.asLog() }
                }
            }

            return AccountResponse(accounts, count = count.toLong(), nextPage = nextPage)

        } catch (e: java.lang.Exception) {
            logcat { e.asLog() }
            return AccountResponse(accounts, count = 0, nextPage = mapOf())
        }

    }

    fun parseFollowees(raw: String): AccountResponse {
        val accounts = mutableListOf<ShortAccount>()
        try {
            val response = JSONObject(raw)
            val data = response.getJSONObject("data")
            val user = data.getJSONObject("user")
            val timeline = user.getJSONObject("edge_follow")

            val count = timeline.getInt("count")
            val nextPage = mutableMapOf<String, String>()
            try {
                val pageInfo = timeline.getJSONObject("page_info")
                nextPage["has_next"] = pageInfo.getBoolean("has_next_page").toString()
                nextPage["end_cursor"] = pageInfo.getString("end_cursor")
            } catch (e: Exception) {
                logcat { e.asLog() }
            }


            val edges = timeline.getJSONArray("edges")

            for (i in 0 until edges.length()) {
                try {
                    val edge: JSONObject = edges.getJSONObject(i)
                    val node: JSONObject = edge.getJSONObject("node")

                    val shortAcc = ShortAccount(
                        id = node.getString("id"),
                        username = node.getString("username"),
                        full_name = node.getString("full_name"),
                        profile_pic_url = node.getString("profile_pic_url"),
                        is_private = node.getBoolean("is_private"),
                        is_verified = node.getBoolean("is_verified"),
                        followed_by_viewer = node.getBoolean("followed_by_viewer"),
                        requested_by_viewer = node.getBoolean("requested_by_viewer"),
                    )
                    accounts.add(shortAcc)

                } catch (e: java.lang.Exception) {
                    logcat { e.asLog() }
                }
            }

            return AccountResponse(accounts, count = count.toLong(), nextPage = nextPage)

        } catch (e: java.lang.Exception) {
            logcat { e.asLog() }
            return AccountResponse(accounts, count = 0, nextPage = mapOf())
        }

    }

    private fun parseProfileFrom(raw: String?): Profile? {
        try {
            return if (raw != null) {
                val response = JSONObject(raw)
                val graphql = response.getJSONObject("graphql")
                val user = graphql.getJSONObject("user")

                val followedByEdge = user.getJSONObject("edge_followed_by")
                val followerCount = followedByEdge.getInt("count")

                val followingByEdge = user.getJSONObject("edge_follow")
                val followingCount = followingByEdge.getInt("count")

                val followedBy = if (user.has("followed_by_viewer")) {
                    user.getBoolean("followed_by_viewer")
                } else {
                    false
                }

                Profile(
                    id = user.getString("id"),
                    fullName = user.getString("full_name"),
                    username = user.getString("username"),
                    bio = user.getString("biography"),
                    profilePic = user.getString("profile_pic_url"),
                    profilePicHD = user.getString("profile_pic_url_hd"),
                    has_ar_effects = user.getBoolean("has_ar_effects"),
                    has_clips = user.getBoolean("has_clips"),
                    has_guides = user.getBoolean("has_guides"),
                    has_channel = user.getBoolean("has_channel"),
                    is_business_account = user.getBoolean("is_business_account"),
                    is_professional_account = user.getBoolean("is_professional_account"),
                    is_joined_recently = user.getBoolean("is_joined_recently"),
                    is_private = user.getBoolean("is_private"),
                    is_verified = user.getBoolean("is_verified"),
                    requested_by_viewer = user.getBoolean("requested_by_viewer"),
                    highlight_reel_count = user.getInt("highlight_reel_count").toLong(),
                    extUrl = user.getString("external_url"),
                    followers = followerCount.toLong(),
                    following = followingCount.toLong(),
                    followed_by_viewer = followedBy,
                )
            } else {
                logcat { "parseTimelineFrom: EMPTY STRING" }
                null
            }

        } catch (e: JSONException) {
            logcat { e.asLog() }
            return null
        }

    }

    private fun parseTimelineFrom(raw: String?): JSONObject? {
        try {
            return if (raw != null) {
                val response = JSONObject(raw)
                val status = response.getString("status")
                val data = response.getJSONObject("data")
                val user = data.getJSONObject("user")
                val timeline = user.getJSONObject("edge_owner_to_timeline_media")
                timeline
            } else {
                logcat { "parseTimelineFrom: EMPTY STRING" }
                null
            }

        } catch (e: JSONException) {
            logcat { e.asLog() }
            return null
        }

    }

    private fun parsePostsFrom(raw: String): PostResponse {
        val posts: MutableList<Post> = mutableListOf()
        val timeline = parseTimelineFrom(raw)
        if (timeline != null) {
            val count = timeline.getInt("count")
            val nextPage = mutableMapOf<String, String>()
            try {
                val pageInfo = timeline.getJSONObject("page_info")
                nextPage["has_next"] = pageInfo.getBoolean("has_next_page").toString()
                nextPage["end_cursor"] = pageInfo.getString("end_cursor")
            } catch (e: Exception) {
                logcat { e.asLog() }
            }

            val edges = timeline.getJSONArray("edges")
            // post extraction
            for (i in 0 until edges.length()) {
                try {
                    val edge: JSONObject = edges.getJSONObject(i)
                    val node: JSONObject = edge.getJSONObject("node")

                    logcat { "GOT A NODE $i :" }

                    // display resource extraction
                    val resources: MutableMap<String, String> = mutableMapOf()
                    try {
                        val displayResources: JSONArray = node.getJSONArray("display_resources")
                        for (r in 0 until displayResources.length()) {
                            val item: JSONObject = displayResources.getJSONObject(r)
                            resources["res"] = item.getInt("config_width").toString()
                            resources["src"] = item.getString("src").toString()
                        }
                    } catch (e: java.lang.Exception) {
                        logcat { e.asLog() }
                    }

                    logcat { "DISPLAY RES $i :" }

                    // caption extraction
                    var captionText = ""
                    try {
                        val captionObj = node.getJSONObject("edge_media_to_caption")
                        val captionEdges = captionObj.getJSONArray("edges")
                        if (captionEdges.length() > 0) {
                            val captionNode = captionEdges[0] as JSONObject
                            captionText = captionNode.getString("text")
                        }
                    } catch (e: JSONException) {
                        logcat { e.asLog() }
                    }

                    logcat { "CAPTION $i : $captionText" }


                    //like count
                    var likes: Long = 0
                    try {
                        val likeObj = node.getJSONObject("edge_media_preview_like")
                        likes = likeObj.getInt("count").toLong()
                    } catch (e: JSONException) {
                        logcat { e.asLog() }
                    }

                    // comment count
                    var comments: Long = 0
                    try {
                        val commentObj = node.getJSONObject("edge_media_to_comment")
                        comments = commentObj.getInt("count").toLong()
                    } catch (e: JSONException) {
                        logcat { e.asLog() }
                    }

                    // type
                    val typeName = node.getString("__typename")
                    var type: Int = 0
                    type = when (typeName) {
                        "GraphImage" -> 0
                        "GraphVideo" -> 1
                        "GraphSidecar" -> 2
                        else -> 3
                    }

                    val post = Post(
                        id = node.getString("id"),
                        typeName = typeName,
                        displayUrl = node.getString("display_url"),
                        resources = resources,
                        isVideo = node.getBoolean("is_video"),
                        captionText = captionText,
                        shortCode = node.getString("shortcode"),
                        commentsDisabled = node.getBoolean("comments_disabled"),
                        takenAtTimestamp = node.getInt("taken_at_timestamp").toLong(),
                        likedByUser = node.getBoolean("viewer_has_liked"),
                        savedByUser = node.getBoolean("viewer_has_saved"),
                        thumbnail = node.getString("thumbnail_src"),
                        likeCount = likes,
                        commentCount = comments,
                        type = type
                    )

                    logcat { "ADDING TO LIST $i :" }
                    posts.add(post)

                } catch (e: JSONException) {
                    // Oops
                    continue
                }
            }
            return PostResponse(posts, nextPage, count.toLong())
        } else {
            return PostResponse(posts, mapOf(), 0)
        }

    }


}

