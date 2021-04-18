package com.ibeybeh.myconsumerapp.db

import android.net.Uri
import android.provider.BaseColumns
import com.ibeybeh.myconsumerapp.utils.Const.AUTHORITY
import com.ibeybeh.myconsumerapp.utils.Const.SCHEME

internal class DatabaseContract {

    internal class UserColumns : BaseColumns {

        companion object {

            const val TABLE_NAME = "user"
            const val ID = "id"
            const val NAME = "name"
            const val LOGIN = "login"
            const val TWITTER_USERNAME = "twitter_username"
            const val EMAIL = "email"
            const val AVATAR = "avatar_url"
            const val BIO = "bio"
            const val COMPANY = "company"
            const val FOLLOWER = "follower"
            const val FOLLOWING = "following"
            const val LOCATION = "location"
            const val PUBLIC_REPO = "public_repo"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}