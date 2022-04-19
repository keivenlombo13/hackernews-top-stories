package com.keiven.hackernewtopstory.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.keiven.hackernewtopstory.helper.ViewBindingUtil

abstract class BaseAdapter<V: ViewBinding, T>(val context: Context) : RecyclerView.Adapter<BaseAdapter<V, T>.ItemViewHolder>() {

    val data: MutableList<T> = ArrayList()
    var selected: T? = null
    var callback: ((T, Int) -> Unit)? = null
    private var curPos: Int? = null
    private var oldPos: Int? = null

    protected open var smoothSelected: Boolean = true
    protected open var enableItemClick: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: V = ViewBindingUtil.inflateGeneric(this, LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = data.size

    open fun setOnClickListener(callback: (item: T, position: Int) -> Unit) {
        this.callback = callback
    }

    protected fun notify(position: Int) {
        if(smoothSelected) {
            curPos = position
            if(oldPos != null) {
                notifyItemChanged(oldPos!!)
                notifyItemChanged(curPos!!)
            } else {
                notifyItemChanged(curPos!!)
            }
            oldPos = curPos
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if(enableItemClick) {
            holder.binding.root.setOnClickListener {
                selected = data.get(position)
                callback?.invoke(data.get(position), position)
                itemClick(it, data.get(position) ,position)
                notify(position)
            }
        }

        bind(holder.binding, data.get(position), position)

        if(selected?.equals(data.get(position)) ?: false) {
            selectedView(holder.binding.root)
            selectedView(holder.binding.root, data.get(position), position)
        } else {
            unselectedView(holder.binding.root)
            unselectedView(holder.binding.root, data.get(position), position)
        }
    }

    open fun setNewData(data: List<T>?) {
        this.data.clear()
        data?.let {
            this.data.addAll(it)
        }
        onSetNewData(this.data)
        notifyDataSetChanged()
    }

    fun setAppendData(data: List<T>?) {
        if(data.isNullOrEmpty().not()) {
            val currentSize = this.data.size
            this.data.addAll(data?.toList()!!)
            notifyItemRangeChanged(currentSize, currentSize + data.size!!)
        }
    }

    open protected fun onSetNewData(data: List<T>) {

    }

    open protected fun itemClick(view: View, item: T, position: Int) {

    }

    open protected fun selectedView(view: View) {

    }

    open protected fun unselectedView(view: View) {

    }

    open protected fun selectedView(view: View, item: T, position: Int) {

    }

    open protected fun unselectedView(view: View, item: T, position: Int) {

    }

    abstract fun bind(binding: V, item: T, position: Int)

    inner class ItemViewHolder(val binding: V) : RecyclerView.ViewHolder(binding.root)
}