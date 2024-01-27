package com.cogniheroid.framework.shared.core.chat.flow

import kotlinx.coroutines.flow.MutableStateFlow

actual class CommonMutableStateFlow<T> actual constructor(
        private val flow: MutableStateFlow<T>
) : MutableStateFlow<T> by flow