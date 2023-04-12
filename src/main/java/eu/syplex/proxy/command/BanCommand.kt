package eu.syplex.proxy.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.punishment.custructor.ShortsConstructor
import eu.syplex.proxy.backend.punishment.custructor.impl.BanReasonConstructor
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import eu.syplex.proxy.util.StringUtil
import ninja.leaping.configurate.ConfigurationNode
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class BanCommand(private val proxyServer: ProxyServer, private val translator: ComponentTranslator, private val configurationNode: ConfigurationNode, private val playerTracker: PlayerTracker) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isEmpty() || args.size == 1) {
            source.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "ban <Name> <ID>"))
            return
        }

        val optionalPlayer = proxyServer.getPlayer(args[0])

        if (!optionalPlayer.isPresent) {
            source.sendMessage(translator.fromConfig("not-online"))
            return
        }

        val target = optionalPlayer.get()
        val proxyPlayer = playerTracker.get(target.username)
        val id = assertIdIsNumeric(args[1], source)

        if (!proxyPlayer.exists()) {
            source.sendMessage(translator.fromConfig("player-not-existent"))
            return
        }

        if (proxyPlayer.isBanned()) {
            source.sendMessage(translator.fromConfig("already-banned"))
            return
        }

        val reason = BanReasonConstructor.construct(configurationNode, id)

        if (reason == null) {
            for (i in 1..15) {
                source.sendMessage(
                    translator.fromConfigWithThreeReplacements(
                        "ban-id-not-found", Placeholder.id, Placeholder.reason, Placeholder.duration, i.toString(),
                        translator.raw("ban-reason-$i"), toDays(translator.raw("ban-duration-$i").toLong())
                    )
                )
            }
            return
        }

        proxyPlayer.ban(getUUID(source), reason, "#${ShortsConstructor.construct()}")
        source.sendMessage(translator.fromConfigWithReplacement("ban-banned", Placeholder.player, target.username))
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<MutableList<String>> {
        val args = invocation.arguments()

        if (args.size == 1) return CompletableFuture.completedFuture(StringUtil.copyPartialMatches(args[0], collectAllPlayerNames(), LinkedList()))
        else if (args.size == 2) return CompletableFuture.completedFuture(StringUtil.copyPartialMatches(args[1], arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"), LinkedList()))

        return CompletableFuture.completedFuture(Collections.emptyList())
    }

    private fun toDays(duration: Long): String {
        if (duration == -1L) return "Permanent"

        val days = TimeUnit.MILLISECONDS.toDays(duration)
        return if (days == 1L) "1 Tag" else "$days Tage"
    }

    private fun collectAllPlayerNames(): LinkedList<String> {
        val names = LinkedList<String>()

        for (player in proxyServer.allPlayers) {
            names.add(player.username)
        }

        return names
    }

    private fun getUUID(source: CommandSource): UUID {
        return when (source) {
            is Player -> source.uniqueId
            else -> UUID.randomUUID()
        }
    }

    private fun assertIdIsNumeric(input: String, source: CommandSource): Int {
        return try {
            input.toInt()

        } catch (e: NumberFormatException) {
            source.sendMessage(translator.fromConfig("id-format-error"))
            -1
        }
    }
}