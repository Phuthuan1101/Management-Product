package com.example.managementuser.data

import androidx.room.TypeConverter
import com.example.managementuser.data.product.Review
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromReviewList(value: List<Review>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toReviewList(value: String): List<Review> {
        val type = object : TypeToken<List<Review>>() {}.type
        return gson.fromJson(value, type)
    }
}
