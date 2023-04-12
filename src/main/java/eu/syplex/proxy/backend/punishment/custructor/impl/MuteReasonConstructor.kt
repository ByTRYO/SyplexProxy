package eu.syplex.proxy.backend.punishment.custructor.impl

import eu.syplex.proxy.backend.punishment.custructor.ReasonConstructor
import eu.syplex.proxy.backend.punishment.custructor.reason.MuteReason
import ninja.leaping.configurate.ConfigurationNode
import java.sql.Timestamp
import java.time.Instant

object MuteReasonConstructor : ReasonConstructor<MuteReason> {

    override fun construct(configurationNode: ConfigurationNode, id: Int): MuteReason? {
        if (id !in 0..5) return null
        return MuteReason(getName(configurationNode, id), getExpiration(configurationNode, id))
    }

    private fun getName(configurationNode: ConfigurationNode, id: Int): String {
        return configurationNode.getNode("mute-reason-${id}").getString(null)
    }

    private fun getExpiration(configurationNode: ConfigurationNode, id: Int): Timestamp? {
        val duration = configurationNode.getNode("mute-duration-${id}").getLong(-1L)
        if (duration == -1L) return null

        return Timestamp.from(Instant.now().plusMillis(duration))
    }
}