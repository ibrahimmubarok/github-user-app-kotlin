package com.ibeybeh.myconsumerapp.db.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var name: String? = null,
    var login: String? = null,
    var twitterUsername: String? = null,
    var email: String? = null,
    var avatarUrl: String? = null,
    var bio: String? = null,
    var company: String? = null,
    var followers: Int = 0,
    var following: Int = 0,
    var location: String? = null,
    var publicRepos: Int = 0
) : Parcelable