package com.keiven.hackernewtopstory.ui.storydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiven.hackernewtopstory.data.repository.Output
import com.keiven.hackernewtopstory.data.repository.StoryRepository
import com.keiven.hackernewtopstory.data.state.StoryDetailState
import com.keiven.hackernewtopstory.data.state.TopStoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(private val repository: StoryRepository) :
    ViewModel() {
    var id: Int? = null

    private val _state = MutableLiveData<StoryDetailState>()
    val state: LiveData<StoryDetailState> = _state

    fun getStoryDetail() {
        _state.value = StoryDetailState(isLoading = true)

        viewModelScope.launch {
            val result = id?.let { repository.getStoryDetail(it) }

            when (result) {
                is Output.Success -> {
                    val detail = result.output
                    val comments = mutableListOf<String>()

                    detail.comments?.map {
                        comments.add(getCommentDetail(it))
                    }
                    _state.postValue(
                        StoryDetailState(
                            storyDetail = result.output,
                            comments = comments
                        )
                    )
                }
                is Output.Error -> {
                    _state.postValue(StoryDetailState(errorCode = result.code))
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