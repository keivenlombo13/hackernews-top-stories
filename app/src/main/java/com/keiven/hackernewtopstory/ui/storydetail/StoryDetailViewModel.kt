package com.keiven.hackernewtopstory.ui.storydetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiven.hackernewtopstory.data.repository.Output
import com.keiven.hackernewtopstory.data.repository.StoryRepository
import com.keiven.hackernewtopstory.data.state.StoryDetailState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoryDetailViewModel @Inject constructor(private val repository: StoryRepository) :
    ViewModel() {
    val storyDetailState = MutableLiveData<StoryDetailState>()

    fun getStoryDetail(id: Int) {
        storyDetailState.value = StoryDetailState(isLoading = true)

        viewModelScope.launch {
            val result = repository.getStoryDetail(id)

            when (result) {
                is Output.Success -> {
                    val detail = result.output
                    val comments = mutableListOf<String>()

                    detail.comments?.map {
                        comments.add(getCommentDetail(it))
                    }
                    storyDetailState.postValue(
                        StoryDetailState(
                            storyDetail = result.output,
                            comments = comments
                        )
                    )
                }
                is Output.Error -> {
                    storyDetailState.postValue(StoryDetailState(errorCode = result.code))
                }
            }
        }
    }

    private suspend fun getCommentDetail(id: Int): String {
        var text = ""

        withContext(Dispatchers.IO) {
            val result = repository.getCommentDetail(id)

            when (result) {
                is Output.Success -> {
                    val comment = result.output.text
                    if (comment != null) {
                        text = comment
                    }
                }
            }
        }

        return text
    }
}