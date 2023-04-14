package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder


class HelpCommand(private val translator: ComponentTranslator) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (sender !is Player) {
            sender.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isNotEmpty()) {
            sender.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "help"))
            return
        }

        translator.listFromConfig("help").forEach { str ->
            sender.sendMessage(str)
        }

    }
}