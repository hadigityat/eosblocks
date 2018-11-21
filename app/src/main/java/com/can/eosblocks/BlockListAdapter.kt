package com.can.eosblocks

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class BlockListAdapter(val list : MutableList<Block>, val context : Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val block = list[p1]
        val titleString = block.producer + " - " + block.blockNumber
        p0.title.text = titleString
    }
}
class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.post_title)
}