package eu.syplex.proxy.config

import eu.syplex.proxy.backend.database.connection.ConnectionProperty
import ninja.leaping.configurate.ConfigurationNode

class PropertyLoader(private val configurationNode: ConfigurationNode) {

    fun loadConnectionProperty(): ConnectionProperty {
        return ConnectionProperty(
            configurationNode.getNode("db-host").getString("127.0.0.1"),
            configurationNode.getNode("db-port").getString("3306"),
            configurationNode.getNode("db-database").getString("proxy"),
            configurationNode.getNode("db-username").getString("root"),
            configurationNode.getNode("db-password").getString(""),
        )
    }

}