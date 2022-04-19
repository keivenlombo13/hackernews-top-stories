package com.keiven.hackernewtopstory.helper

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.reflect.ParameterizedType

class ViewBindingUtil {
    companion object {

        /**
         * @param any : instance of Activity, Fragment, FragmentStateAdapter, or RecyclerView
         */
        fun <V : ViewBinding> inflateGeneric(any: Any, layoutInflater: LayoutInflater): V =
            withGenericBindingClass(any) {
                it.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as V
            }

        /**
         * @param any : instance of Activity, Fragment, FragmentStateAdapter, or RecyclerView
         */
        fun <V : ViewBinding> inflateGeneric(any: Any, parent: ViewGroup): V =
            withGenericBindingClass(any) {
                inflateBinding(it, LayoutInflater.from(parent.context), parent, false)
            }

        /**
         * @param any : instance of Activity, Fragment, FragmentStateAdapter, or RecyclerView
         */
        fun <V : ViewBinding> inflateGeneric(
            any: Any, layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
        ): V =
            withGenericBindingClass(any) {
                inflateBinding(it, layoutInflater, parent, attachToParent)
            }

        /**
         * @param any : instance of Activity, Fragment, FragmentStateAdapter, or RecyclerView
         */
        fun <V : ViewBinding> bindGeneric(any: Any, view: View?): V =
            withGenericBindingClass(any) {
                it.getMethod("bind", View::class.java).invoke(null, view) as V
            }

        private fun <V : ViewBinding> inflateBinding(
            clazz: Class<V>,
            layoutInflater: LayoutInflater,
            parent: ViewGroup?,
            attachToParent: Boolean
        ) =
            clazz.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
                .invoke(null, layoutInflater, parent, attachToParent) as V

        private fun <V : ViewBinding> withGenericBindingClass(any: Any, block: (Class<V>) -> V): V {
            var index = 0
            while (true) {
                try {
                    return block.invoke(any.findGenericBindingClass(index))
                } catch (e: NoSuchMethodException) {
                    index++
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun <V : ViewBinding> Any.findGenericBindingClass(index: Int): Class<V> {
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType && index < type.actualTypeArguments.size) {
                return type.actualTypeArguments[index] as Class<V>
            }
            throw IllegalArgumentException("There is no generic of ViewBinding.")
        }
    }
}