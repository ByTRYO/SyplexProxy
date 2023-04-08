package eu.syplex.proxy.backend.punishment.custructor.impl

import eu.syplex.proxy.backend.exception.IdNotFoundException
import eu.syplex.proxy.backend.punishment.custructor.ReasonConstructor
import eu.syplex.proxy.backend.punishment.custructor.reason.BanReason
import ninja.leaping.configurate.ConfigurationNode
import java.sql.Timestamp
import java.time.Instant

object BanReasonConstructor : ReasonConstructor<BanReason> {

    override fun construct(configurationNode: ConfigurationNode, id: Int): BanReason {
        if (id < 0 || id > 5) throw IdNotFoundException()

        return BanReason(getName(configurationNode, id), getExpiration(configurationNode, id))
    }

    private fun getName(configurationNode: ConfigurationNode, id: Int): String {
        return configurationNode.getNode("ban-reason-${id}").getString("Hacking")
    }

    private fun getExpiration(configurationNode: ConfigurationNode, id: Int): Timestamp? {
        val duration = configurationNode.getNode("ban-duration-${id}").getLong(-1L)
        if (duration == -1L) return null

        return Timestamp.from(Instant.now().plusMillis(duration))
    }


}