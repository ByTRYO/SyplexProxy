package eu.syplex.proxy.backend

import java.util.*

class PlayerTracker {

    private val players = HashMap<UUID, ProxyPlayer>()

    fun track(uuid: UUID, proxyPlayer: ProxyPlayer) {
        players[uuid] = proxyPlayer
        proxyPlayer.register()
    }

    fun get(uuid: UUID): ProxyPlayer? {
        return players.getOrDefault(uuid, null)
    }

}