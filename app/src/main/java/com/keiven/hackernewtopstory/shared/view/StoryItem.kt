package com.keiven.hackernewtopstory.shared.view

import android.content.Intent
import android.widget.TextView
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.keiven.hackernewtopstory.R
import com.keiven.hackernewtopstory.ui.storydetail.StoryDetailActivity

class StoryItem(private val id: Int) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        val itemView = viewHolder.itemView

        itemView.findViewById<TextView>(R.id.tv_title)?.text = id.toString()
        itemView.findViewById<TextView>(R.id.tv_count_comment).text = context.getString(R.string.story_comment_info)
        itemView.findViewById<TextView>(R.id.tv_score).text = context.getString(R.string.story_score_info)
        viewHolder.itemView.setOnClickListener {
            Intent(context, StoryDetailActivity::class.java)
                .apply {
                    putExtra(StoryDetailActivity.ID, id)
                }.also { context.startActivity(it) }

        }
    }

    override fun getLayout() = R.layout.item_col_story
}