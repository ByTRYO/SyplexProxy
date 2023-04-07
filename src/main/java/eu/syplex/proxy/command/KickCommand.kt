package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import net.kyori.adventure.text.Component


class KickCommand(private val server: ProxyServer, private val translator: ComponentTranslator) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) { //Check for valid syntax.
            sender.sendMessage(translator.fromConfigWithReplacement("messages", "invalid-usage", Placeholder.command, "kick <Name> <Grund>"))
            return
        }

        val optionalPlayer = server.getPlayer(args[0])
        if (!optionalPlayer.isPresent) { // check if player is online.
            sender.sendMessage(translator.fromConfig("messages", "not-online"))
            return
        }

        if (args.size == 1) { //custom reason was given.
            optionalPlayer.get().disconnect(translator.fromConfig("messages", "kick"))
            return
        }

        val stringBuilder = StringBuilder()

        for (i in 1 until args.size) { //construct reason
            stringBuilder.append(args[i])
            if (i != args.size - 1) {
                stringBuilder.append(" ")
            }
        }

        optionalPlayer.get().disconnect(Component.text(stringBuilder.toString()))
    }
}