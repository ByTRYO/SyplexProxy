package eu.syplex.proxy.util

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer

class Notifier(private val proxyServer: ProxyServer, private val translator: ComponentTranslator) {

    fun notifyKick(target: Player) {
        for (player: Player in proxyServer.allPlayers) {
            if (!player.hasPermission("proxy.notify.kick")) continue

            player.sendMessage(translator.fromConfigWithTwoReplacements("kick-notify", Placeholder.player, Placeholder.target, player.username, target.username))
            continue
        }
    }

    fun notifyKickWithReason(target: Player, reason: String) {
        for (player: Player in proxyServer.allPlayers) {
            if (!player.hasPermission("proxy.notify.kick")) continue

            player.sendMessage(translator.fromConfigWithThreeReplacements("kick-notify-with-reason", Placeholder.player, Placeholder.target, Placeholder.reason, player.username, target.username, reason))
            continue
        }
    }

}