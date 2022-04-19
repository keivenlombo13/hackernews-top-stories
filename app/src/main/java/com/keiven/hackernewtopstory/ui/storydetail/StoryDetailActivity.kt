package com.keiven.hackernewtopstory.ui.storydetail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.keiven.hackernewtopstory.R
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.data.state.StoryDetailState
import com.keiven.hackernewtopstory.databinding.ActivityMainBinding
import com.keiven.hackernewtopstory.databinding.ActivityStoryDetailBinding
import com.keiven.hackernewtopstory.helper.DateTimeHelper
import com.keiven.hackernewtopstory.helper.constants.StatusCode
import com.keiven.hackernewtopstory.shared.extensions.hide
import com.keiven.hackernewtopstory.shared.extensions.show
import com.keiven.hackernewtopstory.ui.topstory.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryDetailActivity : AppCompatActivity() {

    companion object {
        const val ID = "id"
    }

    private val viewModel: StoryDetailViewModel by viewModels()
    private var storyDetail: StoryDetail? = null
    private lateinit var adapter: StoryDetailAdapter
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        viewModel.id = intent.getIntExtra(ID, 0)
        viewModel.getStoryDetail()
        viewModel.state.observe(this, storyDetailStateObserver)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ivFavorite.setOnClickListener {
            Intent(this, MainActivity::class.java)
                .apply {
                    putExtra(MainActivity.STORY, storyDetail)
                    putExtra(MainActivity.LAST_CLICKED, DateTimeHelper.getLastTime())
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                }.also {
                    startActivity(it)
                    finish()
                }
        }

        adapter = StoryDetailAdapter(this)
        binding.rvComment.adapter = adapter
        binding.rvComment.isNestedScrollingEnabled = false

        binding.layoutError.btnTryAgain.setOnClickListener {
            viewModel.getStoryDetail()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val storyDetailStateObserver = Observer<StoryDetailState> {
        if (it.isLoading) {
            binding.progressBar.show()
            binding.layoutError.llErrorContainer.hide()
            binding.svStoryDetail.hide()
            return@Observer
        }else {
            binding.progressBar.hide()
        }

        if (it.errorCode != StatusCode.NO_ERROR) {
            binding.layoutError.llErrorContainer.show()
            binding.layoutError.tvErrorMessage.text =
                if (it.errorCode == StatusCode.GENERAL_ERROR) getString(R.string.general_error_message) else getString(
                    R.string.network_error_message
                )
            binding.svStoryDetail.hide()
            return@Observer
        }

        binding.progressBar.hide()
        binding.layoutError.llErrorContainer.hide()
        binding.svStoryDetail.show()
        storyDetail = it.storyDetail
        if (it.storyDetail != null) {
            binding.tvTitle.text = it.storyDetail.title
            binding.tvAuthor.text = "by ${it.storyDetail.author}"
            binding.tvDate.text = DateTimeHelper.convertTimestampToReadableTime(it.storyDetail.time)

            if (it.comments.isNotEmpty()) {
                binding.tvEmptyComment.hide()
                binding.layoutCommentList.show()
                adapter.setNewData(it.comments)
            } else {
                binding.tvEmptyComment.show()
                binding.layoutCommentList.hide()
            }
        }
    }
}
