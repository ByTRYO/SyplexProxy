package eu.syplex.proxy.util

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.backend.punishment.custructor.reason.ReportReason
import net.kyori.adventure.text.event.ClickEvent

class Notifier(private val proxyServer: ProxyServer, private val translator: ComponentTranslator) {

    fun notifyReport(target: Player, reason: ReportReason, serverName: String) {
        for (player in proxyServer.allPlayers) {
            if (!player.hasPermission("proxy.notify.report")) continue

            player.sendMessage(translator.fromConfigWithTwoReplacements("notify-report", Placeholder.target, Placeholder.reason, target.username, reason.name)
                .append(translator.fromConfig("notify-report-clickable").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/server $serverName"))))
        }
    }

}