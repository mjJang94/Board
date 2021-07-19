package com.mj.board.ui.adapter

import android.app.AlertDialog
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mj.board.R
import com.mj.board.database.BoardEntity
import androidx.recyclerview.widget.ListAdapter
import com.mj.board.databinding.BoardItemBinding

class MainAdapter : ListAdapter<BoardEntity, MainAdapter.MainHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BoardEntity>() {
            override fun areItemsTheSame(oldItem: BoardEntity, newItem: BoardEntity): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: BoardEntity, newItem: BoardEntity): Boolean {
                return oldItem.uid == newItem.uid && oldItem.title == newItem.title
            }
        }
    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("stickerItems")
        fun RecyclerView.setItems(items: List<BoardEntity>?) {
            val adapter = adapter as? MainAdapter
                ?: MainAdapter().also { adapter = it }
            adapter.submitList(items ?: return)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = BoardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            lifecycleOwner = parent.findViewTreeLifecycleOwner()

            val view = LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false)
            val lp: GridLayoutManager.LayoutParams =
                view.layoutParams as GridLayoutManager.LayoutParams
            lp.height = parent.measuredHeight / 3
            lp.width = parent.measuredWidth / 2

            root.layoutParams = lp
        }
        return MainHolder(
            binding
        ) {
            binding.item = it
        }
    }

    override fun onBindViewHolder(mainHolder: MainHolder, position: Int) {

        mainHolder.update(getItem(position))
    }

    class MainHolder(binding: BoardItemBinding,val update: (item: BoardEntity) -> Unit) : RecyclerView.ViewHolder(binding.root)
}



