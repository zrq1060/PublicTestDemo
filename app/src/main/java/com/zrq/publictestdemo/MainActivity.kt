package com.zrq.publictestdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 轮询发送消息
        findViewById<View>(R.id.button0).setOnClickListener {
            viewModel.loopSend()
        }

        // stateFlow-接收
        setButtonClickCollect(R.id.button1, viewModel.stateFlow) {
            sout("collect=stateFlow=$it")
            if (it != null) {
                // 先通知，防止内部没展示，继续发送。
                viewModel.setStateFlowMessageShown()
                showToast(it.toString())
            }
        }

        // stateFlowList-接收
        setButtonClickCollect(R.id.button2, viewModel.stateFlowList) {
            sout("collect=stateFlowList=$it")

            if (!it.isNullOrEmpty()) {
                // 先通知，防止内部没展示，继续发送。
                viewModel.setStateFlowListMessageShown()
                showToast(it.first().toString())
            }
        }
        // channel-接收
        setButtonClickCollect(R.id.button3, viewModel.channel.receiveAsFlow()) {
            sout("collect=channel=$it")
            if (it != null) {
                // 先通知，防止内部没展示，继续发送。
                viewModel.setChannelMessageShown()
                showToast(it.toString())
            }
        }
        // sharedFlow-接收
        setButtonClickCollect(R.id.button4, viewModel.sharedFlow) {
            sout("collect=sharedFlow=$it")
            if (it != null) {
                // 先通知，防止内部没展示，继续发送。
                viewModel.setSharedFlowMessageShown()
                showToast(it.toString())
            }
        }
        // sharedFlowList-接收
        setButtonClickCollect(R.id.button5, viewModel.sharedFlowList) {
            sout("collect=sharedFlowList=$it")
        }
    }

    private suspend fun showToast(message: String) {
        sout("showToast=message=$message")
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        delay(2500)
    }

    private fun <T> setButtonClickCollect(id: Int, flow: Flow<T>, collector: FlowCollector<T>) {
        findViewById<View>(id).setOnClickListener {
            // 接收消息
            lifecycleScope.launch {
                flow.flowWithLifecycle(lifecycle)
                    .collect(collector)
            }
        }
    }
}

fun sout(s: String) {
    Log.e("MainActivity", s)
}