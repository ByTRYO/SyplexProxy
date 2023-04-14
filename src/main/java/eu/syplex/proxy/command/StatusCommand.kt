package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import net.kyori.adventure.text.Component


class StatusCommand(private val translator: ComponentTranslator, private val tracker: PlayerTracker) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (sender !is Player) {
            sender.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.size != 1) {
            sender.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "status <name>"))
            return
        }

        val target = tracker.get(args[0])
        val optBans = target.countBans()
        val optMutes = target.countMutes()

        if (optBans.isEmpty && optMutes.isEmpty) {
            sender.sendMessage(translator.fromConfig("status-error"))
            return
        }
        //Status Nachricht
        sender.sendMessage(translator.fromConfigWithReplacement("status-header", Placeholder.player, args[0]))
        sender.sendMessage(Component.empty())
        sender.sendMessage(translator.fromConfigWithReplacement("status-ban-message", Placeholder.count, "${optBans.get()}"))
        sender.sendMessage(translator.fromConfigWithReplacement("status-mute-message", Placeholder.count, "${optMutes.get()}"))
        sender.sendMessage(Component.empty())
        sender.sendMessage(translator.fromConfig("status-footer"))

    }
}