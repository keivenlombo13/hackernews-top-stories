package com.keiven.hackernewtopstory.data.state

import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.helper.constants.StatusCode

data class TopStoryState(
    val isLoading: Boolean = false,
    val errorCode: Int = StatusCode.NO_ERROR
)