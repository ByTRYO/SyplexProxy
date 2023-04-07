package eu.syplex.proxy.util

import com.velocitypowered.api.command.Command
import eu.syplex.proxy.ProxyPlugin

class CommandPool(private val plugin: ProxyPlugin) {

    fun register(command: Command, name: String, vararg alias: String) {
        val commandManager = plugin.proxyServer.commandManager
        val commandMeta = commandManager.metaBuilder(name).aliases(*alias).plugin(plugin).build()

        commandManager.register(commandMeta, command)
    }

}