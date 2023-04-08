package eu.syplex.proxy.backend.punishment.entry

import java.sql.Timestamp
import java.util.*

abstract class Entry(open val uuid: UUID, open val date: Timestamp, open val expires: Timestamp?, open val reason: String)