package com.ibeybeh.myconsumerapp.db.helper

import android.database.Cursor
import com.ibeybeh.myconsumerapp.db.DatabaseContract.UserColumns
import com.ibeybeh.myconsumerapp.db.entity.User

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()
        var user: User

        cursor?.apply {
            while (moveToNext()) {
                user = User()
                user.id = getInt(getColumnIndexOrThrow(UserColumns.ID))
                user.name = getString(getColumnIndexOrThrow(UserColumns.NAME))
                user.login = getString(getColumnIndexOrThrow(UserColumns.LOGIN))
                user.twitterUsername = getString(getColumnIndexOrThrow(UserColumns.TWITTER_USERNAME))
                user.email = getString(getColumnIndexOrThrow(UserColumns.EMAIL))
                user.avatarUrl = getString(getColumnIndexOrThrow(UserColumns.AVATAR))
                user.bio = getString(getColumnIndexOrThrow(UserColumns.BIO))
                user.company = getString(getColumnIndexOrThrow(UserColumns.COMPANY))
                user.followers = getInt(getColumnIndexOrThrow(UserColumns.FOLLOWER))
                user.following = getInt(getColumnIndexOrThrow(UserColumns.FOLLOWING))
                user.location = getString(getColumnIndexOrThrow(UserColumns.LOCATION))
                user.publicRepos = getInt(getColumnIndexOrThrow(UserColumns.PUBLIC_REPO))
                userList.add(user)
            }
        }
        return userList
    }
}