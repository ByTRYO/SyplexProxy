package eu.syplex.proxy.backend

import eu.syplex.proxy.backend.impl.OfflineProxyPlayer
import eu.syplex.proxy.backend.impl.ProxyPlayer
import java.util.*

class PlayerTracker {

    private val players = HashMap<UUID, ProxyPlayer>()

    fun track(uuid: UUID, proxyPlayer: ProxyPlayer) {
        players[uuid] = proxyPlayer
        proxyPlayer.register()
    }

    fun get(uuid: UUID): ProxyPlayer? {
        return players[uuid]
    }

    fun get(name: String): ProxiedPlayer {
        for (proxyPlayer in players.values) {
            val proxyPlayerName = proxyPlayer.name().orElse(null) ?: continue

            if (!proxyPlayerName.equals(name, false)) continue
            return proxyPlayer
        }

        return OfflineProxyPlayer(name)
    }

}