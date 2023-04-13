package eu.syplex.proxy.backend.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.TimeUtil

class ChatListener(private val playerTracker: PlayerTracker, private val translator: ComponentTranslator) {

    @Subscribe
    fun onChat(event: PlayerChatEvent) {
        val player = event.player
        val proxyPlayer = playerTracker.get(player.uniqueId) ?: return

        if (!proxyPlayer.isMuted()) return

        val muteEntry = proxyPlayer.getMuteEntry().get()
        val expires = muteEntry.expires

        if (TimeUtil.checkTimeElapsed(expires)) {
            proxyPlayer.unmute()
            return
        }
    }

}