package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.util.ComponentTranslator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class PingCommand(private val translator: ComponentTranslator) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("messages", "no-player"))
            return
        }

        if (args.isNotEmpty()) {
            source.sendMessage(Component.text("Bitte benutze ").color(NamedTextColor.RED).append(Component.text("/ping").color(NamedTextColor.GOLD)))
            return
        }

        source.sendMessage(Component.text("Dein Ping beträgt ").color(NamedTextColor.GRAY).append(Component.text("${source.ping}ms").color(NamedTextColor.GOLD)))
    }
}