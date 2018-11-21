package com.can.eosblocks

interface FragmentCallbacks {

    /**
     * Get the list of blocks from the API
     */
    fun onShowBlocksButtonClick()

    fun onBlocksTaskStarted()

    fun onBlocksTaskFinished(resultCode : Int, list : MutableList<Block>)




}