package com.keiven.hackernewtopstory.ui.topstory

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.keiven.hackernewtopstory.R
import com.keiven.hackernewtopstory.data.model.StoryDetail
import com.keiven.hackernewtopstory.data.repository.Output
import com.keiven.hackernewtopstory.databinding.ItemColStoryBinding
import com.keiven.hackernewtopstory.ui.base.BaseAdapter
import com.keiven.hackernewtopstory.ui.storydetail.StoryDetailActivity

class MainActivityAdapter(context: Context): BaseAdapter<ItemColStoryBinding, Output<StoryDetail>>(context) {
    override fun bind(binding: ItemColStoryBinding, item: Output<StoryDetail>, position: Int) {
        when (item) {
            is Output.Success -> {
                binding.tvTitle.text = item.output.title
                binding.tvCountComment.text = item.output.countComments.toString()
                binding.tvScore.text = context.getString(R.string.story_score_info, item.output.score.toString())
            }
        }
    }
}