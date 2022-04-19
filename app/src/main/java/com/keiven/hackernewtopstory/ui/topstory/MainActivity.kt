package com.keiven.hackernewtopstory.ui.topstory

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.keiven.hackernewtopstory.R
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.data.state.TopStoryState
import com.keiven.hackernewtopstory.di.Injector
import com.keiven.hackernewtopstory.helper.constants.StatusCode
import com.keiven.hackernewtopstory.shared.extensions.hide
import com.keiven.hackernewtopstory.shared.extensions.show
import com.keiven.hackernewtopstory.shared.view.StoryItem
import com.keiven.hackernewtopstory.ui.base.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    companion object {
        const val STORY = "story"
        const val LAST_CLICKED = "last_clicked"
    }

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory<MainViewModel>
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var story: StoryDetail? = null
    private var lastClicked: String? = null
    private lateinit var layoutProgressBar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        story = intent.getParcelableExtra(STORY)
        lastClicked = intent.getStringExtra(LAST_CLICKED)

        Injector.getApp().inject(this)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.topStoryState.observe(this, topStoryStateObserver)

        registerProgressBar()

        rv_story.layoutManager = GridLayoutManager(this, 2)
        rv_story.adapter = adapter

        btn_try_again.setOnClickListener {
            viewModel.getTopStory()
        }

        viewModel.getTopStory()
    }

    private val topStoryStateObserver = Observer<TopStoryState> {
        if (it.isLoading) {
            layoutProgressBar.show()
            layout_error.hide()
            layout_story_list.hide()
            return@Observer
        }
        if (it.errorCode != StatusCode.NO_ERROR) {
            layoutProgressBar.hide()
            layout_error.show()
            tv_error_message.text =
                if (it.errorCode == StatusCode.GENERAL_ERROR) getString(R.string.general_error_message) else getString(
                    R.string.network_error_message
                )
            layout_story_list.hide()
            return@Observer
        }

        layoutProgressBar.hide()
        layout_error.hide()
        if (story != null) {
            layout_favorite_story.show()
            tv_story_title.text = story?.title
            tv_story_last_clicked.text = lastClicked
        } else {
            layout_favorite_story.hide()
        }
        it.stories.map {
            adapter.add(StoryItem(it))
        }
        layout_story_list.show()
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
