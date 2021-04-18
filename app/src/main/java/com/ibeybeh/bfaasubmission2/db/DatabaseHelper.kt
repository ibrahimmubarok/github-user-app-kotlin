package com.ibeybeh.bfaasubmission2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ibeybeh.bfaasubmission2.db.DatabaseContract.UserColumns
import com.ibeybeh.bfaasubmission2.db.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "db_github_user"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                "(${UserColumns.ID} INTEGER PRIMARY KEY," +
                "${UserColumns.NAME} TEXT NOT NULL," +
                "${UserColumns.LOGIN} TEXT NOT NULL," +
                "${UserColumns.TWITTER_USERNAME} TEXT NOT NULL," +
                "${UserColumns.EMAIL} TEXT NOT NULL," +
                "${UserColumns.AVATAR} TEXT NOT NULL," +
                "${UserColumns.BIO} TEXT NOT NULL," +
                "${UserColumns.COMPANY} TEXT NOT NULL," +
                "${UserColumns.FOLLOWER} INTEGER NOT NULL," +
                "${UserColumns.FOLLOWING} INTEGER NOT NULL," +
                "${UserColumns.LOCATION} TEXT NOT NULL," +
                "${UserColumns.PUBLIC_REPO} INTEGER NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}