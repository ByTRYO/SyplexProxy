package eu.syplex.proxy.backend.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PostLoginEvent
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.impl.ProxyPlayer
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import eu.syplex.proxy.util.TimeUtil
import net.kyori.adventure.text.Component


class PostLoginListener(private val playerTracker: PlayerTracker, private val translator: ComponentTranslator) {

    @Subscribe
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        val proxyPlayer = ProxyPlayer(player, translator)

        playerTracker.track(player.uniqueId, proxyPlayer)

        if (!proxyPlayer.isBanned()) return

        val banEntry = proxyPlayer.getBanEntry().get()
        val expires = banEntry.expires

        if (TimeUtil.checkTimeElapsed(expires)) {
            proxyPlayer.unban()
            return
        }

        if (expires != null) {
            player.disconnect(
                translator.fromConfigWithReplacement("ban-screen-line-1", Placeholder.duration, TimeUtil.getDurationString(banEntry.date, banEntry.expires)).append(Component.newline())
                    .append(translator.fromConfigWithTwoReplacements("ban-screen-line-2", Placeholder.reason, Placeholder.uid, banEntry.reason, banEntry.id))
                    .append(Component.newline()).append(Component.newline())
                    .append(translator.fromConfigWithReplacement("ban-screen-time-left", Placeholder.expires, TimeUtil.createReadableTimeString(banEntry.expires)))
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

}