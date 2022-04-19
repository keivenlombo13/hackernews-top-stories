package com.keiven.hackernewtopstory.ui.topstory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiven.hackernewtopstory.data.repository.Output
import com.keiven.hackernewtopstory.data.repository.StoryRepository
import com.keiven.hackernewtopstory.data.state.TopStoryState
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: StoryRepository) :
    ViewModel() {
    val topStoryState = MutableLiveData<TopStoryState>()

    fun getTopStory() {
        topStoryState.value = TopStoryState(isLoading = true)

        viewModelScope.launch {
            val result = repository.getTopStory()

            when (result) {
                is Output.Success -> {
                    topStoryState.postValue(
                        TopStoryState(
                            stories = result.output
                        )
                    )
                }
                is Output.Error -> {
                    topStoryState.postValue(TopStoryState(errorCode = result.code))
                }
            }
        }
    }
}