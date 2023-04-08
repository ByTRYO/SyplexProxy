package eu.syplex.proxy.backend.punishment.entry.impl

import eu.syplex.proxy.backend.punishment.entry.Entry
import java.sql.Timestamp
import java.util.*

data class BanEntry(override val uuid: UUID, override val date: Timestamp, override val expires: Timestamp?, override val reason: String, val id: String) : Entry(uuid, date, expires, reason)