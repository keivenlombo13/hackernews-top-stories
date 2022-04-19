package com.keiven.hackernewtopstory.data.repository

import com.keiven.hackernewtopstory.data.model.CommentDetail
import com.keiven.hackernewtopstory.data.model.StoryDetail

interface StoryRepository {
    suspend fun getTopStory(): Output<IntArray>

    suspend fun getStoryDetail(id: Int): Output<StoryDetail>

    suspend fun getCommentDetail(id: Int): Output<CommentDetail>
}