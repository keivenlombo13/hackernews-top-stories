package com.keiven.hackernewtopstory.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryDetail(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("by")
    val author: String,

    @SerializedName("score")
    val score: Int,

    @SerializedName("time")
    val time: Long,

    @SerializedName("descendants")
    val countComments: Int,

    @SerializedName("kids")
    var comments: List<Int>?
) : Parcelable