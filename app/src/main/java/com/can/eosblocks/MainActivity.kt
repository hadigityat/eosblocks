package com.can.eosblocks

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity(), FragmentCallbacks {

    private val workerFragment = WorkerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val progressLayout = findViewById<LinearLayout>(R.id.progress_layout)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, MainFragment()).commit()

        workerFragment.mListener = this
        workerFragment.getInfo()

    }


    override fun onBlocksTaskStarted() {
        progress_layout.visibility = View.VISIBLE
    }

    override fun onBlocksTaskFinished(resultCode: Int, list: MutableList<Block>) {
        progress_layout.visibility = View.GONE

        when(resultCode) {
            com.can.eosblocks.ERROR_CODE -> {
                Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show()
            }

            com.can.eosblocks.SUCCESS_CODE -> {
                val listFragment = BlockListFragment()
                listFragment.setList(list)
                supportFragmentManager.beginTransaction().
                    replace(R.id.main_frame, listFragment).addToBackStack("1").commit()
            }

        }
    }

    override fun onShowBlocksButtonClick() {
        workerFragment.getBlocks()
    }
}