package com.keiven.hackernewtopstory.ui.storydetail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.keiven.hackernewtopstory.R
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.data.state.StoryDetailState
import com.keiven.hackernewtopstory.di.Injector
import com.keiven.hackernewtopstory.helper.DateTimeHelper
import com.keiven.hackernewtopstory.helper.constants.StatusCode
import com.keiven.hackernewtopstory.shared.extensions.hide
import com.keiven.hackernewtopstory.shared.extensions.show
import com.keiven.hackernewtopstory.shared.view.CommentItem
import com.keiven.hackernewtopstory.ui.base.BaseViewModelFactory
import com.keiven.hackernewtopstory.ui.topstory.MainActivity
import kotlinx.android.synthetic.main.activity_story_detail.*
import kotlinx.android.synthetic.main.content_story_detail.*
import kotlinx.android.synthetic.main.layout_error.*
import javax.inject.Inject

class StoryDetailActivity : AppCompatActivity() {

    companion object {
        const val ID = "id"
    }

    private lateinit var viewModel: StoryDetailViewModel

    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory<StoryDetailViewModel>
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var storyDetail: StoryDetail? = null
    private lateinit var layoutProgressBar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)

        Injector.getApp().inject(this)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(StoryDetailViewModel::class.java)
        viewModel.storyDetailState.observe(this, storyDetailStateObserver)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(ID, 0)

        registerProgressBar()

        iv_favorite.setOnClickListener {
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

        rv_comment.layoutManager = LinearLayoutManager(this)
        rv_comment.adapter = adapter
        rv_comment.isNestedScrollingEnabled = false

        btn_try_again.setOnClickListener {
            viewModel.getStoryDetail(id)
        }

        viewModel.getStoryDetail(id)
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
            layoutProgressBar.show()
            layout_error.hide()
            sv_story_detail.hide()
            return@Observer
        }
        if (it.errorCode != StatusCode.NO_ERROR) {
            layoutProgressBar.hide()
            layout_error.show()
            tv_error_message.text =
                if (it.errorCode == StatusCode.GENERAL_ERROR) getString(R.string.general_error_message) else getString(
                    R.string.network_error_message
                )
            sv_story_detail.hide()
            return@Observer
        }
        val detail = it.storyDetail
        storyDetail = detail

        layoutProgressBar.hide()
        layout_error.hide()
        sv_story_detail.show()
        if (detail != null) {
            tv_title.text = detail.title
            tv_author.text = "by ${detail.author}"
            tv_date.text = DateTimeHelper.convertTimestampToReadableTime(detail.time)

            if (it.comments.isNotEmpty()) {
                tv_empty_comment.hide()
                layout_comment_list.show()
                it.comments.map {
                    adapter.add(CommentItem(it))
                }
            } else {
                tv_empty_comment.show()
                layout_comment_list.hide()
            }
        }
    }


    private fun registerProgressBar() {
        layoutProgressBar = LinearLayout(this)
        layoutProgressBar.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutProgressBar.orientation = LinearLayout.VERTICAL

        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.isIndeterminate = true

        layoutProgressBar.addView(progressBar)
        frame_layout.addView(layoutProgressBar)
    }
}
