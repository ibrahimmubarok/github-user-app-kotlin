package com.ibeybeh.bfaasubmission2.db.helper

import android.database.Cursor
import com.ibeybeh.bfaasubmission2.db.DatabaseContract.UserColumns
import com.ibeybeh.bfaasubmission2.db.entity.User

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

    fun mapCursorToObject(cursor: Cursor?): User {
        var user = User()
        try {
            cursor?.apply {
                moveToFirst()
                user = User(
                    id = getInt(getColumnIndexOrThrow(UserColumns.ID)),
                    name = getString(getColumnIndexOrThrow(UserColumns.NAME)),
                    login = getString(getColumnIndexOrThrow(UserColumns.LOGIN)),
                    twitterUsername = getString(getColumnIndexOrThrow(UserColumns.TWITTER_USERNAME)),
                    email = getString(getColumnIndexOrThrow(UserColumns.EMAIL)),
                    avatarUrl = getString(getColumnIndexOrThrow(UserColumns.AVATAR)),
                    bio = getString(getColumnIndexOrThrow(UserColumns.BIO)),
                    company = getString(getColumnIndexOrThrow(UserColumns.COMPANY)),
                    followers = getInt(getColumnIndexOrThrow(UserColumns.FOLLOWER)),
                    following = getInt(getColumnIndexOrThrow(UserColumns.FOLLOWING)),
                    location = getString(getColumnIndexOrThrow(UserColumns.LOCATION)),
                    publicRepos = getInt(getColumnIndexOrThrow(UserColumns.PUBLIC_REPO))
                )
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return user
    }
}