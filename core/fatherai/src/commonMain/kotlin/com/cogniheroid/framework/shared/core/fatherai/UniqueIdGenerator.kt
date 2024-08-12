package com.cogniheroid.framework.shared.core.fatherai

import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.traceFormatDefault

class UniqueIdGenerator(private val initialValue: suspend () -> Long) {

    private var atomicLong: AtomicLong? = null

    suspend fun get(): Long {
        if(atomicLong == null) {
            atomicLong = atomic(initialValue())
        }
       return atomicLong!!.incrementAndGet()
    }

}