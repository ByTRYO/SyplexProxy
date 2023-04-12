package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder

class UnbanCommand(private val translator: ComponentTranslator, private val tracker: PlayerTracker) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.size != 1) {
            source.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "unban <Name>"))
            return
        }

        val name = args[0]
        val proxyPlayer = tracker.get(name)

        if (!proxyPlayer.exists()) {
            source.sendMessage(translator.fromConfig("player-not-existent"))
            return
        }

        if (!proxyPlayer.isBanned()) {
            source.sendMessage(translator.fromConfig("not-banned"))
            return
        }

        proxyPlayer.unban(source)
        source.sendMessage(translator.fromConfigWithReplacement("player-unbanned", Placeholder.player, name))
    }
}