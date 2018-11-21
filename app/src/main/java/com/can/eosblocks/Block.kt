package com.can.eosblocks


/**
 * Class which provides a model for a block
 * @constructor Sets all properties of the block
 * @property producer the name of the producer
 * @property signature the unique signature string of the producer
 * @property count the count of transactions
 *  @property blockNumber the block number
 *  @property transaction the object that holds raw contents of transaction

 */

data class Block(val producer : String, val signature : String, val count : Int, val blockNumber : Int, val transaction : Transaction)