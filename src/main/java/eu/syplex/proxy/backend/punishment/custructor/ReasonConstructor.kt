package eu.syplex.proxy.backend.punishment.custructor

import ninja.leaping.configurate.ConfigurationNode

interface ReasonConstructor<T> {

    fun construct(configurationNode: ConfigurationNode, id: Int): T

}