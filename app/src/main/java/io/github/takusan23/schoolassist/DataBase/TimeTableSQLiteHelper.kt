package io.github.takusan23.schoolassist.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TimeTableSQLiteHelper (val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(
            SQL_CREATE_ENTRIES
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // アップデートの判別
        db.execSQL(
            SQL_DELETE_ENTRIES
        )
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // データーベースのバージョン
        private val DATABASE_VERSION = 1

        // データーベース名
        private val DATABASE_NAME = "TimeTable.db"
        private val TABLE_NAME = "timetable_db"
        private val SETTING = "setting"
        private val SUBJECT = "subject"
        private val TIME = "time"
        private val WEEK = "week"
        private val MEMO = "memo"
        private val TAGS = "tags"
        private val _ID = "_id"


        // , を付け忘れるとエラー
        private val SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                WEEK + " TEXT ," +
                SUBJECT + " TEXT ," +
                TIME + " INTEGER ," +
                MEMO + " TEXT ," +
                TAGS + " TEXT ," +
                SETTING + " TEXT" +
                ")"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}