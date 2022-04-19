package com.keiven.hackernewtopstory.data.repository

import com.keiven.hackernewtopstory.data.model.CommentDetail
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.data.services.ApiService
import com.keiven.hackernewtopstory.helper.constants.StatusCode
import java.io.IOException
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    StoryRepository {
    override suspend fun getTopStory(): Output<IntArray> {
        return try {
            val result = apiService.getTopStory()
            Output.Success(result)
        } catch (e: IOException) {
            Output.Error(StatusCode.NETWORK_ERROR)
        } catch (e: Exception) {
            Output.Error(StatusCode.GENERAL_ERROR)
        }
    }

    override suspend fun getStoryDetail(id: Int): Output<StoryDetail> {
        return try {
            val result = apiService.getStoryDetail(id)
            Output.Success(result)
        } catch (e: IOException) {
            Output.Error(StatusCode.NETWORK_ERROR)
        } catch (e: Exception) {
            Output.Error(StatusCode.GENERAL_ERROR)
        }
    }

    override suspend fun getCommentDetail(id: Int): Output<CommentDetail> {
        return try {
            val result = apiService.getCommentDetail(id)
            Output.Success(result)
        } catch (e: IOException) {
            Output.Error(StatusCode.NETWORK_ERROR)
        } catch (e: Exception) {
            Output.Error(StatusCode.GENERAL_ERROR)
        }
    }
}