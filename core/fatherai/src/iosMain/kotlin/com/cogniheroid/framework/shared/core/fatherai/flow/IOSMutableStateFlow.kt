package com.cogniheroid.framework.shared.core.fatherai.flow

import kotlinx.coroutines.flow.MutableStateFlow

class IOSMutableStateFlow<T>(
        initialValue: T
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue))