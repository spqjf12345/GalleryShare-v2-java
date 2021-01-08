package com.example.project1_final.model
import android.database.Cursor

class Record {
    var name: String = "NULL"
    var score: Int = 0
    var time: Int = 0

    companion object {
        fun fromCursor(cursor: Cursor?): Record {
            val score = Record()
            score.name = cursor!!.getString(cursor.getColumnIndex("name"))
            score.score = cursor!!.getInt(cursor.getColumnIndex("score"))
            score.time = cursor!!.getInt(cursor.getColumnIndex("time"))
            return score
        }
    }
}