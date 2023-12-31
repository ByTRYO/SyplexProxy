package eu.syplex.proxy.backend.punishment.entry.impl

import eu.syplex.proxy.backend.punishment.entry.Entry
import java.sql.Timestamp
import java.util.*

data class KickEntry(override val uuid: UUID, override val date: Timestamp, override val reason: String) : Entry(uuid, date, null, reason)