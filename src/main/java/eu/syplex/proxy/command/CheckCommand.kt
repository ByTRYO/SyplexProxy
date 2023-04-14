package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import net.kyori.adventure.text.TextComponent


class CheckCommand(private val translator: ComponentTranslator, private val tracker: PlayerTracker) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (sender !is Player) {
            sender.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.size > 1 || args.isEmpty()) {
            sender.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "check <name>"))
            return
        }

        val player = tracker.get(args[0])
        if (!(player.exists())) {
            sender.sendMessage(translator.fromConfig("player-not-existent"))
            return
        }

        sender.sendMessage(translator.fromConfig("player-ban-check").append(setBanAnswer(player.isBanned())))
        sender.sendMessage(translator.fromConfig("player-mute-check").append(setMuteAnswer(player.isMuted())))

    }

    private fun setBanAnswer(worth: Boolean): TextComponent {
        return if (worth)
            translator.fromConfig("is-banned")
        else
            translator.fromConfig("not-banned")
    }

    private fun setMuteAnswer(worth: Boolean): TextComponent {
        return if (worth)
            translator.fromConfig("is-muted")
        else
            translator.fromConfig("not-muted")
    }
}