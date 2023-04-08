package eu.syplex.proxy.backend.punishment.custructor

import java.util.concurrent.ThreadLocalRandom

object ShortsConstructor {

    private const val upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val lowerCase = "abcdefghijklmnopqrstuvwxyz"
    private const val digits = "0123456789"

    private const val alphaNumeric = upperCase + lowerCase + digits

    private val random = ThreadLocalRandom.current()
    private lateinit var symbols: CharArray
    private lateinit var buffer: CharArray

    fun construct(): String {
        this.buffer = CharArray(9)
        this.symbols = alphaNumeric.toCharArray()

        for (i in buffer.indices) {
            buffer[i] = symbols[random.nextInt(symbols.size)]
        }

        return String(buffer)
    }
}