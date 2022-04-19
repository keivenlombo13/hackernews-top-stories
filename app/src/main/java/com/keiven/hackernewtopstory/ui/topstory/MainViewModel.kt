package com.keiven.hackernewtopstory.ui.topstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.data.repository.Output
import com.keiven.hackernewtopstory.data.repository.StoryRepository
import com.keiven.hackernewtopstory.data.state.TopStoryState
import com.keiven.hackernewtopstory.helper.constants.StatusCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: StoryRepository) :
    ViewModel() {
    private val _topStoryState = MutableLiveData<TopStoryState>()
    val topStoryState: LiveData<TopStoryState> = _topStoryState

    private val _storyList = MutableLiveData<List<Output<StoryDetail>>>()
    val storyList: LiveData<List<Output<StoryDetail>>> = _storyList

    fun getTopStory() {
        _topStoryState.value = TopStoryState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getTopStory()
            when (result) {
                is Output.Success -> {
                    val deferreds: MutableList<Deferred<Output<StoryDetail>>> = ArrayList()
                    result.output.forEach {
                        deferreds.add(async {repository.getStoryDetail(it)})
                    }
                    val storyDetailList: List<Output<StoryDetail>> = deferreds.awaitAll()
                    _storyList.postValue(storyDetailList)
                    _topStoryState.postValue(TopStoryState(isLoading = false))
                }
                is Output.Error -> {
                    _topStoryState.postValue(TopStoryState(errorCode = result.code, isLoading = false))
                }
            }
        }
    }
}