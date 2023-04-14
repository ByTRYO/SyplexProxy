package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.punishment.custructor.ShortsConstructor
import eu.syplex.proxy.backend.punishment.custructor.impl.ReportReasonConstructor
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Notifier
import eu.syplex.proxy.util.Placeholder
import eu.syplex.proxy.util.StringUtil
import ninja.leaping.configurate.ConfigurationNode
import java.util.*
import java.util.concurrent.CompletableFuture

class ReportCommand(private val translator: ComponentTranslator, private val proxyServer: ProxyServer, private val configurationNode: ConfigurationNode, private val tracker: PlayerTracker, private val notifier: Notifier) :
    SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.size != 2) {
            source.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "report <Name> <Grund>"))
            return
        }

        val optionalPlayer = proxyServer.getPlayer(args[0])

        if (!optionalPlayer.isPresent) {
            source.sendMessage(translator.fromConfig("not-online"))
            return
        }

        val reason = ReportReasonConstructor.construct(configurationNode, args[1])

        if (reason == null) {
            for (i in 1..7) {
                source.sendMessage(translator.fromConfigWithReplacement("report-reason-not-found", Placeholder.reason, translator.raw("report-reason-$i")))
            }
            return
        }

        val target = optionalPlayer.get()
        val proxyPlayer = tracker.get(target.uniqueId) ?: return
        val serverName = source.currentServer.get().serverInfo.name

        proxyPlayer.report(reason, source.uniqueId, serverName, "#${ShortsConstructor.construct()}")
        source.sendMessage(translator.fromConfig("report-reported"))
        notifier.notifyReport(target, reason, serverName)
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<MutableList<String>> {
        val args = invocation.arguments()

        if (args.size == 1) return CompletableFuture.completedFuture(StringUtil.copyPartialMatches(args[0], collectAllPlayerNames(), LinkedList()))
        return CompletableFuture.completedFuture(Collections.emptyList())
    }

    private fun collectAllPlayerNames(): LinkedList<String> {
        val names = LinkedList<String>()

        for (player in proxyServer.allPlayers) {
            names.add(player.username)
        }

        return names
    }
}