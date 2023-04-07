package eu.syplex.proxy.backend.punishment.impl

import eu.syplex.proxy.backend.punishment.Entry
import java.sql.Timestamp
import java.util.*

data class MuteEntry(override val uuid: UUID, override val date: Timestamp, override val expires: Float, override val reason: String, private val id: String) : Entry(uuid, date, expires, reason)



