package com.keiven.hackernewtopstory.shared.view

import android.widget.TextView
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.keiven.hackernewtopstory.R

class CommentItem(private val text: String) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val textView = viewHolder.itemView.findViewById<TextView>(R.id.tv_comment)
        textView.text = text
    }

    override fun getLayout() = R.layout.item_row_comment
}