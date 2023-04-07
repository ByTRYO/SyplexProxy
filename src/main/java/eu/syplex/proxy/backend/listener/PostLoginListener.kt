package eu.syplex.proxy.backend.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PostLoginEvent
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.ProxyPlayer
import eu.syplex.proxy.util.Notifier

class PostLoginListener(private val notifier: Notifier, private val playerTracker: PlayerTracker) {

    @Subscribe
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        playerTracker.track(player.uniqueId, ProxyPlayer(player, notifier))
    }

}