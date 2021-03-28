package com.ahmetgezici.githubrepos.utils

import android.app.Activity
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.io.Reader
import java.text.SimpleDateFormat
import java.util.*

class Tools {

    companion object{

        fun getLanguageColor(activity: Activity): Map<String, String> {

            val typeToken: TypeToken<Map<String, String>> = object : TypeToken<Map<String, String>>() {}
            val reader: Reader = InputStreamReader(activity.assets.open("language_color.json"))

            return Gson().fromJson(reader, typeToken.type)
        }

        ///////////////

        fun convertDate(date: String): String {

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

            val newDate: Date? = simpleDateFormat.parse(date)
            val newDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR"))

            return newDateFormat.format(newDate)
        }

        ///////////////

        fun dpToPx(dp: Float): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun pxToDp(px: Double): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }

    }

}