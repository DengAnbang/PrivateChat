package com.hezeyi.common.net

/**
 * Created by dab on 2021/9/27 15:03
 */
sealed class LoadState {
    object Loading : LoadState()
    object LoadStart : LoadState()
}