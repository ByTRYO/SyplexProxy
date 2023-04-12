package eu.syplex.proxy.backend.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PostLoginEvent
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.impl.ProxyPlayer
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import net.kyori.adventure.text.Component
import java.sql.Timestamp
import java.util.concurrent.TimeUnit
import kotlin.math.abs


class PostLoginListener(private val playerTracker: PlayerTracker, private val translator: ComponentTranslator) {

    @Subscribe
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        val proxyPlayer = ProxyPlayer(player, translator)

        playerTracker.track(player.uniqueId, proxyPlayer)

        if (!proxyPlayer.isBanned()) return

        val banEntry = proxyPlayer.getBanEntry().get()
        val expires = banEntry.expires

        if (checkUnban(expires)) {
            proxyPlayer.unban()
            return
        }

        if (expires != null) {
            player.disconnect(
                translator.fromConfigWithReplacement("ban-screen-line-1", Placeholder.duration, getDuration(banEntry.date, banEntry.expires)).append(Component.newline())
                    .append(translator.fromConfigWithTwoReplacements("ban-screen-line-2", Placeholder.reason, Placeholder.uid, banEntry.reason, banEntry.id))
                    .append(Component.newline()).append(Component.newline())
                    .append(translator.fromConfigWithReplacement("ban-screen-time-left", Placeholder.expires, calculateTime(banEntry.expires)))
                    .append(Component.newline()).append(Component.newline())
                    .append(translator.fromConfigWithReplacement("ban-screen-line-3", Placeholder.url, translator.raw("unban-url")))
            )
            return
        }

        player.disconnect(
            translator.fromConfigWithReplacement("ban-screen-line-1", Placeholder.duration, "PERMANENT").append(Component.newline())
                .append(translator.fromConfigWithTwoReplacements("ban-screen-line-2", Placeholder.reason, Placeholder.uid, banEntry.reason, banEntry.id))
                .append(Component.newline()).append(Component.newline())
                .append(translator.fromConfigWithReplacement("ban-screen-line-3", Placeholder.url, translator.raw("unban-url")))
        )
    }

    private fun checkUnban(expires: Timestamp?): Boolean {
        if (expires == null) return false
        return expires.time <= System.currentTimeMillis()
    }

    private fun calculateTime(timestamp: Timestamp): String {
        var restTime = ""
        val diff = abs((timestamp.time - System.currentTimeMillis()) / 1000)

        val days = (diff / 86400).toInt()
        val hours = ((diff / 3600) % 24).toInt()
        val minutes = ((diff / 60) % 60).toInt()
        val seconds = (diff % 60).toInt()

        if (days > 0) restTime += if (days > 1) "$days Tage" else " 1 Tag"
        if (hours > 0) restTime += if (hours > 1) " $hours Stunden" else " 1 Stunde"
        if (minutes > 0) restTime += if (minutes > 1) " $minutes Minuten" else " 1 Minute"
        if (seconds > 0) restTime += if (seconds > 1) " $seconds Sekunden" else " 1 Sekunde"

        return restTime
    }

    private fun getDuration(date: Timestamp, expires: Timestamp?): String {
        if (expires == null) return "PERMANENT"

        val diff = abs(expires.time - date.time)
        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

        if (days == 1L) return "1 Tag"
        return "$days Tage"
    }

}