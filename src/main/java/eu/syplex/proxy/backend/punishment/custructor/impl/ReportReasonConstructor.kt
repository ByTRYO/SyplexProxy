package eu.syplex.proxy.backend.punishment.custructor.impl

import eu.syplex.proxy.backend.punishment.custructor.ReasonConstructor
import eu.syplex.proxy.backend.punishment.custructor.reason.ReportReason
import ninja.leaping.configurate.ConfigurationNode

object ReportReasonConstructor : ReasonConstructor<ReportReason> {

    fun construct(configurationNode: ConfigurationNode, name: String): ReportReason? {
        val id = idFromName(configurationNode, name)
        return construct(configurationNode, id)
    }

    override fun construct(configurationNode: ConfigurationNode, id: Int): ReportReason? {
        if (id !in 0..7) return null
        return ReportReason(getName(configurationNode, id))
    }

    private fun getName(configurationNode: ConfigurationNode, id: Int): String {
        return configurationNode.getNode("report-reason-${id}").getString("Hacking")
    }

    private fun idFromName(configurationNode: ConfigurationNode, name: String): Int {
        return when {
            name.equals(configurationNode.getNode("report-reason-1").getString(null), true) -> 1
            name.equals(configurationNode.getNode("report-reason-2").getString(null), true) -> 2
            name.equals(configurationNode.getNode("report-reason-3").getString(null), true) -> 3
            name.equals(configurationNode.getNode("report-reason-4").getString(null), true) -> 4
            name.equals(configurationNode.getNode("report-reason-5").getString(null), true) -> 5
            name.equals(configurationNode.getNode("report-reason-6").getString(null), true) -> 6
            name.equals(configurationNode.getNode("report-reason-7").getString(null), true) -> 7
            else -> -1
        }
    }
}