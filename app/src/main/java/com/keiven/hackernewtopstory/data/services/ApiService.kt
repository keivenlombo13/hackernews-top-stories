package com.keiven.hackernewtopstory.data.services

import com.keiven.hackernewtopstory.data.model.CommentDetail
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.helper.constants.Endpoint
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(Endpoint.TOP_STORY)
    suspend fun getTopStory(): IntArray

    @GET(Endpoint.STORY_DETAIL)
    suspend fun getStoryDetail(@Path(Endpoint.STORY_ID) id: Int): StoryDetail

    @GET(Endpoint.COMMENT_DETAIL)
    suspend fun getCommentDetail(@Path(Endpoint.COMMENT_ID) id: Int): CommentDetail
}