package com.keiven.hackernewtopstory.ui.storydetail

import android.content.Context
import com.keiven.hackernewtopstory.databinding.ItemRowCommentBinding
import com.keiven.hackernewtopstory.ui.base.BaseAdapter

class StoryDetailAdapter(context: Context): BaseAdapter<ItemRowCommentBinding, String>(context){
    override fun bind(binding: ItemRowCommentBinding, item: String, position: Int) {
        binding.tvComment.text = item
    }
}