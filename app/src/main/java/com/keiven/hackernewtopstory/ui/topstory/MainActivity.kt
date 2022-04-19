package com.keiven.hackernewtopstory.ui.topstory

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.keiven.hackernewtopstory.R
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.data.repository.Output
import com.keiven.hackernewtopstory.data.state.TopStoryState
import com.keiven.hackernewtopstory.databinding.ActivityMainBinding
import com.keiven.hackernewtopstory.helper.constants.StatusCode
import com.keiven.hackernewtopstory.shared.extensions.hide
import com.keiven.hackernewtopstory.shared.extensions.show
import com.keiven.hackernewtopstory.ui.storydetail.StoryDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val STORY = "story"
        const val LAST_CLICKED = "last_clicked"
    }

    private val viewModel: MainViewModel by viewModels()
    private var story: StoryDetail? = null
    private var lastClicked: String? = null
    private lateinit var adapter: MainActivityAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MainActivityAdapter(this)
        binding.rvStory.adapter = adapter

        story = intent.getParcelableExtra(STORY)
        lastClicked = intent.getStringExtra(LAST_CLICKED)

        viewModel.getTopStory()
        viewModel.topStoryState.observe(this, topStoryStateObserver)
        viewModel.storyList.observe(this) {
            adapter.setNewData(it)
        }

        binding.layoutError.btnTryAgain.setOnClickListener {
            viewModel.getTopStory()
        }

        if (story != null) {
            binding.layoutFavoriteStory.show()
            binding.tvStoryTitle.text = story?.title
            binding.tvStoryLastClicked.text = lastClicked
        } else {
            binding.layoutFavoriteStory.hide()
        }

        adapter.setOnClickListener { item, position ->
            if(item is Output.Success) {
                Intent(this, StoryDetailActivity::class.java)
                    .apply {

                        putExtra(StoryDetailActivity.ID, item.output.id)
                    }.also { startActivity(it) }
            }
        }
    }

    private val topStoryStateObserver = Observer<TopStoryState> {
        if (it.isLoading) {
            binding.progressBar.show()
            binding.layoutError.llErrorContainer.hide()
            binding.layoutStoryList.hide()
            return@Observer
        } else {
            binding.progressBar.hide()
        }

        if (it.errorCode != StatusCode.NO_ERROR) {
            binding.layoutError.llErrorContainer.show()
            binding.layoutError.tvErrorMessage.text =
                if (it.errorCode == StatusCode.GENERAL_ERROR) getString(R.string.general_error_message) else getString(
                    R.string.network_error_message
                )
            binding.layoutStoryList.hide()
            return@Observer
        }

        binding.layoutError.llErrorContainer.hide()
        binding.layoutStoryList.show()
    }
}
