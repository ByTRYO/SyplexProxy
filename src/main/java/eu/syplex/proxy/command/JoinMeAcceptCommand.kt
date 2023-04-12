package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.cloudnetservice.driver.registry.ServiceRegistry
import eu.cloudnetservice.modules.bridge.node.CloudNetBridgeModule
import eu.cloudnetservice.modules.bridge.player.PlayerManager
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.JoinMeValidator
import eu.syplex.proxy.util.Placeholder


class JoinMeAcceptCommand(private val translator: ComponentTranslator, private val validator: JoinMeValidator) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (sender !is Player) {
            sender.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isNotEmpty()) {
            sender.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "accept"))
            return
        }

        if (!validator.isCooldownActive()) {
            sender.sendMessage(translator.fromConfig("joinme-not-active"))
            return
        }

        val manager = ServiceRegistry.first(PlayerManager::class.java) ?: return
        manager.onlinePlayer(sender.uniqueId)?.playerExecutor()?.connect(validator.getServer().serverInfo.name)

    }
}