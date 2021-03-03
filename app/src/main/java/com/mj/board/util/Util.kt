package com.mj.board.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.mj.board.database.BoradDB
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Util {

    companion object {

        @SuppressLint("SimpleDateFormat")
        fun getTodayDate(): String{
            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy년mm월dd일")

            return dateFormat.format(Date(time))
        }

        @SuppressLint("SimpleDateFormat")
        fun getTime(): String{
            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("HH:mm")

            return dateFormat.format(Date(time))
        }
    }
}