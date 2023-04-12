package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder

class TeamChatCommand(private val proxyServer: ProxyServer, private val translator: ComponentTranslator) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isEmpty()) {
            source.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "teamchat <Nachricht>"))
            return
        }

        val raw = StringBuilder()
        for (arg in args) {
            raw.append(arg).append(" ")
        }

        val message = translator.fromConfigWithTwoReplacements("teamchat", Placeholder.player, Placeholder.message, source.username, raw.toString())

        for (player in proxyServer.allPlayers) {
            if (!player.hasPermission("proxy.command.teamchat")) continue

            player.sendMessage(message)
        }
    }
}