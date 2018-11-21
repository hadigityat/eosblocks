package com.can.eosblocks

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class BlockListFragment : Fragment() {

    private lateinit var blockList : MutableList<Block>

    fun setList(list : MutableList<Block>) {
        blockList = list
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val listView = view.findViewById<RecyclerView>(R.id.blocksListView)

        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = BlockListAdapter(blockList, activity as Context)
        return view
    }




}