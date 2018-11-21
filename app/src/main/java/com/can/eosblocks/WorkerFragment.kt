package com.can.eosblocks

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

class WorkerFragment : Fragment() {

    var blockList : MutableList<Block> = ArrayList(20)

    var headBlock : Int = 28051178

    var mListener : FragmentCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    fun getInfo(){
        Log.d("WorkerFragment", "getting info..." )

        GetInfoTask().execute()
    }

    fun getBlocks() {
        mListener?.onBlocksTaskStarted()
        GetBlocksTask().execute()
    }

    inner class GetInfoTask : AsyncTask<Unit, Unit, String>() {
        override fun doInBackground(vararg p0: Unit?): String {

            var urlConnection: HttpURLConnection? = null
            var result = ""

            try {


                val url = URL(INFO_URL)

                urlConnection = url.openConnection() as HttpsURLConnection
                urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true


                val postData: ByteArray = result.toByteArray(StandardCharsets.UTF_8)

                urlConnection.setRequestProperty("charset", "utf-8")
                urlConnection.setRequestProperty("Content-length", postData.size.toString())
                urlConnection.setRequestProperty("Content-Type", "application/json")

                try {
                    val outputStream = DataOutputStream(urlConnection.outputStream)
                    outputStream.write(postData)
                    outputStream.flush()
                } catch (exception: Exception) {
                    Log.e("WorkerFragment", "Error : "  +exception.toString() )
                    mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)
                }
                urlConnection.connect()

                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    try {

                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    result = reader.use(BufferedReader::readText)


                } catch (exception: Exception) {
                    Log.e("WorkerFragment", "Error : " + exception.toString())

                    mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)

                }
                }
                else {
                    try {

                        val reader = BufferedReader(InputStreamReader(urlConnection.errorStream))
                        val error = reader.use(BufferedReader::readText)
                        Log.e("WorkerFragment", "Error : $error")


                    } catch (exception: Exception) {
                        Log.e("WorkerFragment", "Error : " + exception.toString())

                        mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)

                    }
                }


            } catch (ex: Exception) {
                Log.e("WorkerFragment", "Error : " + ex.toString())
                mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }

            return result
        }

        override fun onPostExecute(result: String?) {
            Log.d("WorkerFragment", "result of info : $result" )

            if(!result.isNullOrEmpty()) {
                val json = JSONObject(result)

                headBlock = json.get("head_block_num") as Int
            }


            Log.d("WorkerFragment", "head block:  $headBlock" )

        }
    }

    inner class GetBlocksTask : AsyncTask<Unit, String, String>() {
        override fun doInBackground(vararg p0: Unit?): String {
            var urlConnection: HttpURLConnection? = null
            var result = ""
            val finish = headBlock
            val start = finish.minus(19)
            for(i in start..finish) {


                try {
                    val url = URL(BLOCKS_URL)

                    urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                    urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                    urlConnection.requestMethod = "POST"
                    urlConnection.doOutput = true


                    var reqParam = URLEncoder.encode("block_num_or_id", "UTF-8") + "=" + URLEncoder.encode(i.toString(), "UTF-8")
                    //var reqParam = URLEncoder.encode(i.toString(), "UTF-8")
                    Log.d("WorkerFragment", "request: $reqParam")

                    urlConnection.setRequestProperty("charset", "utf-8")
                    urlConnection.setRequestProperty("Content-Type", "application/json")

                    try {
                        val outputStream = DataOutputStream(urlConnection.outputStream)
                        val wr = OutputStreamWriter(outputStream)
                        wr.write(reqParam)

                        //outputStream.write(postData)
                        outputStream.flush()
                    } catch (exception: Exception) {
                        Log.e("WorkerFragment", "Error : "  +exception.toString() )
                        mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)
                    }

                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        try {

                            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                            result = reader.use(BufferedReader::readText)
                            publishProgress(result)

                        } catch (exception: Exception) {

                            Log.e("WorkerFragment", "Error : " + exception.toString())

                            mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)

                        }
                    }
                    else {
                        try {

                            val reader = BufferedReader(InputStreamReader(urlConnection.errorStream))
                            val error = reader.use(BufferedReader::readText)
                            Log.e("WorkerFragment", "Error : $error")


                        } catch (exception: Exception) {
                            Log.e("WorkerFragment", "Error : " + exception.toString())

                            mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)

                        }
                    }


                } catch (ex: Exception) {
                    Log.e("WorkerFragment", "Error : "  +ex.toString() )
                    mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect()
                        break
                    }
                }
            }

            return "done"
        }

        override fun onProgressUpdate(vararg values: String?) {
            //super.onProgressUpdate(*values)
            Log.d("WorkerFragment", "progress values: $values")
            try {
                val json = JSONObject(values[0])


                val producer = json.get("producer") as String
                val producerSignature = json.get("producer_signature") as String
                val transactions = json.getJSONArray("regions").getJSONObject(0).
                    getJSONArray("cycles_summary").getJSONObject(0).getJSONArray("transactions")
                val numOfTransactions = transactions.length()
                val blockNum = json.get("block_num") as Int

                val tranactionID = transactions.getJSONObject(0).get("id") as String
                val transactionStatus = transactions.getJSONObject(0).get("status") as String

                val block = Block(producer, producerSignature, numOfTransactions, blockNum, Transaction(transactionStatus, tranactionID))
                blockList.add(block)

            } catch (ex: Exception) {
                Log.e("WorkerFragment", "Error parsing block json : "  +ex.toString() )
                mListener?.onBlocksTaskFinished(com.can.eosblocks.ERROR_CODE, blockList)
            }


        }

        override fun onPostExecute(result: String?) {
            //super.onPostExecute(result)
            if(blockList.isEmpty()) {
                mListener?.onBlocksTaskFinished(ERROR_CODE, blockList)
            } else
            mListener?.onBlocksTaskFinished(SUCCESS_CODE, blockList)


        }

    }
}



