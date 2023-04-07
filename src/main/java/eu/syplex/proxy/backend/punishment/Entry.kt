package eu.syplex.proxy.backend.punishment

import java.sql.Timestamp
import java.util.*

abstract class Entry(open val uuid: UUID, open val date: Timestamp, open val expires: Float?, open val reason: String)