package com.can.eosblocks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class MainFragment : Fragment() {

    private lateinit var mListener : FragmentCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListener = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val showBlocksButton = view.findViewById<Button>(R.id.btn_show_recent)


        showBlocksButton.setOnClickListener {
            mListener.onShowBlocksButtonClick()
        }

        return view
    }
}