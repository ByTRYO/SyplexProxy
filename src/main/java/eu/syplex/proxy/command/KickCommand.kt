package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.ProxyPlayer
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Notifier
import eu.syplex.proxy.util.Placeholder
import net.kyori.adventure.text.Component


class KickCommand(private val server: ProxyServer, private val translator: ComponentTranslator, private val playerTracker: PlayerTracker) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) { //Check for a valid syntax
            sender.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "kick <Name> <Grund>"))
            return
        }

        val optionalPlayer = server.getPlayer(args[0])
        if (!optionalPlayer.isPresent) { // Check if player is online
            sender.sendMessage(translator.fromConfig("not-online"))
            return
        }

        val target = optionalPlayer.get()
        val proxyPlayer = playerTracker.get(target.uniqueId)

        if(proxyPlayer == null) {
            sender.sendMessage(translator.fromConfig("not-online"))
            return
        }

        if (args.size == 1) { // No custom reason was given
            proxyPlayer.kick(translator.fromConfig("kick-disconnect"))
            return
        }

        val builder = StringBuilder()
        for (i in 1 until args.size) { // Construct the custom reason
            builder.append(args[i])
            if (i != args.size - 1) {
                builder.append(" ")
            }
        }

        val reason = builder.toString()

        proxyPlayer.kick(Component.text(reason))
    }
}