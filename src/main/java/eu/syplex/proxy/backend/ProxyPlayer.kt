package eu.syplex.proxy.backend

import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.backend.database.sadu.StaticQueryAdapter
import eu.syplex.proxy.backend.punishment.History
import eu.syplex.proxy.backend.punishment.impl.BanEntry
import eu.syplex.proxy.backend.punishment.impl.KickEntry
import eu.syplex.proxy.backend.punishment.impl.MuteEntry
import eu.syplex.proxy.util.Notifier
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import java.sql.Timestamp
import java.time.Instant
import java.util.*

class ProxyPlayer(private val player: Player, private val notifier: Notifier) {

    fun isBanned(): Optional<Boolean> {
        return StaticQueryAdapter.builder(Boolean::class.java)
            .query("SELECT * FROM player_bans WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .map()
            .firstSync()
    }

    fun isMuted(): Optional<Boolean> {
        return StaticQueryAdapter.builder(Boolean::class.java)
            .query("SELECT * FROM player_mutes WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .map()
            .firstSync()
    }

    fun ban(uid: UUID, expires: Float, reason: String, id: String) {
        StaticQueryAdapter.builder()
            .query("INSERT INTO player_bans (uuid, executor_uuid, expires, reason, ban_uid) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(player.uniqueId.toString())
                stmt.setString(uid.toString())
                stmt.setFloat(expires)
                stmt.setString(reason)
                stmt.setString(id)
            }
            .insert()
            .send()

        History.createEntry(player.uniqueId, BanEntry(player.uniqueId, Timestamp.from(Instant.now()), expires, reason, id))
        TODO("Implement notification")
    }

    fun mute(uid: UUID, expires: Float, reason: String, id: String) {
        StaticQueryAdapter.builder()
            .query("INSERT INTO player_mutes (uuid, executor_uuid, expires, reason, mute_uid) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(player.uniqueId.toString())
                stmt.setString(uid.toString())
                stmt.setFloat(expires)
                stmt.setString(reason)
                stmt.setString(id)
            }
            .insert()
            .send()

        History.createEntry(player.uniqueId, MuteEntry(player.uniqueId, Timestamp.from(Instant.now()), expires, reason, id))
        TODO("Implement notification")
    }

    fun kick(reason: TextComponent) {
        player.disconnect(reason)

        val translated = MiniMessage.miniMessage().stripTags(reason.content())

        History.createEntry(player.uniqueId, KickEntry(player.uniqueId, Timestamp.from(Instant.now()), translated))
        notifier.notifyKickWithReason(player, translated)
    }

    fun unban() {
        StaticQueryAdapter.builder()
            .query("DELETE FROM player_bans WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .delete()
            .send()
    }

    fun unmute() {
        StaticQueryAdapter.builder()
            .query("DELETE FROM player_mutes WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .delete()
            .send()
    }


}