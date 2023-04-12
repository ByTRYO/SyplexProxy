package eu.syplex.proxy.backend.punishment.custructor.reason

import java.sql.Timestamp

data class MuteReason(val name: String, val expires: Timestamp?)
