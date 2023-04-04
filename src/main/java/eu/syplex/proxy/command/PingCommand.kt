package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class PingCommand : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(Component.text("Dazu musst du ein Spieler sein!").color(NamedTextColor.RED))
            return
        }

        if (args.isNotEmpty()) {
            source.sendMessage(Component.text("Bitte benutze ").color(NamedTextColor.RED).append(Component.text("/ping").color(NamedTextColor.GOLD)))
            return
        }

        source.sendMessage(Component.text("Dein Ping beträgt ").color(NamedTextColor.GRAY).append(Component.text("${source.ping}ms").color(NamedTextColor.GOLD)))
    }
}