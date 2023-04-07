package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder

class PingCommand(private val translator: ComponentTranslator) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isNotEmpty()) {
            source.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "ping"))
            return
        }

        source.sendMessage(translator.fromConfigWithReplacement("ping", Placeholder.ping, "${source.ping}"))
    }
}