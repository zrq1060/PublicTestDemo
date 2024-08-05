package com.zrq.publictestdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/8/2 下午4:45
 */
class MainViewModel : ViewModel() {
    val stateFlow = MutableStateFlow<Int?>(null)
    val stateFlowList = MutableStateFlow<List<Int>?>(null)
    val channel = Channel<Int?>()
    val sharedFlow = MutableSharedFlow<Int?>()
    val sharedFlowList = MutableSharedFlow<List<Int>?>()
    private var sharedFlowListCollect: List<Int>? = null

    init {
        viewModelScope.launch {
            sharedFlowList.collect {
                sharedFlowListCollect = it
            }
        }
    }

    fun loopSend() {
        viewModelScope.launch {
            repeat(100) { index ->

//                stateFlow.update {
//                    sout("=============send==stateFlow=$index")
//                    index
//                }
                stateFlowList.update {
                    val result = (stateFlowList.value ?: listOf()) + index
                    sout("=============send==stateFlowList=$index=result=$result")
                    result
                }
//                viewModelScope.launch {
//                    sout("=============send==channel=$index")
//                    channel.send(index)
//                }
//                viewModelScope.launch {
//                    sout("=============send==sharedFlow=$index")
//                    sharedFlow.emit(index)
//                }
//
//                viewModelScope.launch {
////                sout("=============send==sharedFlowList=$index")
////                val oldList = sharedFlowList.firstOrNull() ?: listOf()
//
//                    sout("=============send==sharedFlowListCollect=$index")
//                    val oldList = sharedFlowListCollect ?: listOf()
//
//                    val newList = oldList + index
//                    sharedFlowList.emit(newList)
//                }
                // 延迟
                delay(1000)
            }
        }
    }

    fun setStateFlowMessageShown() {
        sout("setStateFlowMessageShown")
        stateFlow.update {
            null
        }
    }

    fun setStateFlowListMessageShown() {
        sout("setStateFlowListMessageShown")
        stateFlowList.update {
            if (it != null && it.size > 1) {
                // 有数据，移除
                it.subList(1, it.size)
            } else {
                // 无数据或1个数据，返回空。
                null
            }
        }
    }

    fun setChannelMessageShown() {
        // channel不需要通知消息是否展示，因为它不会重放，所以不需要。
//        sout("setChannelMessageShown")
    }

    fun setSharedFlowMessageShown() {
        // SharedFlow不需要通知消息是否展示，因为它默认不会重放，所以不需要。
//        sout("setSharedFlowMessageShown")
    }
}