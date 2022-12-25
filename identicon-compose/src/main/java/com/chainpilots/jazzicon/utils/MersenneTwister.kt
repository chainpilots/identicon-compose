package com.chainpilots.jazzicon.utils

class MersenneTwister(
    seed: UInt
) {
    private val n = 624
    private val m = 397
    private val matrixA = 0x9908b0dfu
    private val upperMask = 0x80000000u
    private val lowerMask = 0x7fffffffu
    private val mt = Array(624) { 0.toUInt() }
    private var mti = n+1

    init {
        initSeed(seed)
    }

    private fun initSeed(seed: UInt) {
        mt[0] = seed.shr(0)
        mti = 1
        while (mti<n) {
            val s = mt[mti-1].xor(mt[mti-1].shr(30))
            mt[mti] = (((((s.and(0xffff0000u)).shr(16)) * 1812433253u).shl(16)) + (s.and( 0x0000ffffu)) * 1812433253u) + mti.toUInt()
            mt[mti] = mt[mti].shr(0)
            mti++
        }
    }

    private fun randomInt(): UInt {
        var y: UInt
        val mag01 = arrayOf(0x0u, matrixA)
        if (mti >= n) {
            var kk = 0
            if (mti == n+1) {
                initSeed(5489u)
            }
            while (kk<n-m) {
                y = mt[kk].and(upperMask).or(mt[kk+1].and(lowerMask))
                mt[kk] = mt[kk+m].xor(y.shr(1).xor(mag01[y.and(0x1u).toInt()]))
                kk++
            }
            while (kk<n-1) {
                y=mt[kk].and(upperMask).or(mt[kk+1].and(lowerMask))
                mt[kk] = mt[kk+(m-n)].xor(y.shr(1).xor(mag01[y.and(0x1u).toInt()]))
                kk++
            }
            y = mt[n-1].and(upperMask).or(mt[0].and(lowerMask))
            mt[n-1] = mt[m-1].xor(y.shr(1)).xor(mag01[y.and( 0x1u).toInt()])

            mti = 0
        }

        y = mt[mti++]

        y=y.xor(y.shr(11))
        y=y.xor(y.shl(7).and(0x9d2c5680u))
        y=y.xor(y.shl(15).and(0xefc60000u))
        y=y.xor(y.shr(18))

        return y.shr(0)
    }

    fun random(): Double {
        return randomInt().toDouble() * (1.0/4294967295.0)
    }
}