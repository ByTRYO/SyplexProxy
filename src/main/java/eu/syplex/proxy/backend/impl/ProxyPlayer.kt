package eu.syplex.proxy.backend.impl

import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.backend.ProxiedPlayer
import eu.syplex.proxy.backend.database.sadu.StaticQueryAdapter
import eu.syplex.proxy.backend.punishment.custructor.reason.BanReason
import eu.syplex.proxy.backend.punishment.custructor.reason.MuteReason
import eu.syplex.proxy.backend.punishment.custructor.reason.ReportReason
import eu.syplex.proxy.backend.punishment.entry.History
import eu.syplex.proxy.backend.punishment.entry.impl.BanEntry
import eu.syplex.proxy.backend.punishment.entry.impl.KickEntry
import eu.syplex.proxy.backend.punishment.entry.impl.MuteEntry
import eu.syplex.proxy.util.ComponentTranslator
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import java.sql.Timestamp
import java.time.Instant
import java.util.*

class ProxyPlayer(private val player: Player, private val translator: ComponentTranslator) : ProxiedPlayer {

    override fun exists(): Boolean {
        return true
    }

    fun register() {
        StaticQueryAdapter.builder()
            .query("INSERT INTO players (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name=?;")
            .parameter { stmt ->
                stmt.setString(player.uniqueId.toString())
                stmt.setString(player.username)
                stmt.setString(player.username)
            }
            .insert()
            .send()
    }

    override fun isBanned(): Boolean {
        return StaticQueryAdapter.builder(Boolean::class.java)
            .query("SELECT uuid FROM player_bans WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .readRow { row -> row.getString("uuid") != null }
            .firstSync().orElse(false)
    }

    override fun isMuted(): Boolean {
        return StaticQueryAdapter.builder(Boolean::class.java)
            .query("SELECT uuid FROM player_mutes WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .readRow { row -> row.getString("uuid") != null }
            .firstSync().orElse(false)
    }

    override fun ban(uid: UUID, reason: BanReason, id: String) {
        StaticQueryAdapter.builder()
            .query("INSERT INTO player_bans (uuid, executor_uuid, expires, reason, ban_uid) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(player.uniqueId.toString())
                stmt.setString(uid.toString())
                stmt.setTimestamp(reason.expires)
                stmt.setString(reason.name)
                stmt.setString(id)
            }
            .insert()
            .send()

        History.createEntry(player.uniqueId, BanEntry(player.uniqueId, Timestamp.from(Instant.now()), reason.expires, reason.name, id))
        player.disconnect(translator.fromConfig("ban-disconnect"))
    }

    override fun mute(uid: UUID, reason: MuteReason, id: String) {
        StaticQueryAdapter.builder()
            .query("INSERT INTO player_mutes (uuid, executor_uuid, expires, reason, mute_uid) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(player.uniqueId.toString())
                stmt.setString(uid.toString())
                stmt.setTimestamp(reason.expires)
                stmt.setString(reason.name)
                stmt.setString(id)
            }
            .insert()
            .send()

        History.createEntry(player.uniqueId, MuteEntry(player.uniqueId, Timestamp.from(Instant.now()), reason.expires, reason.name, id))
    }

    fun kick(reason: TextComponent) {
        player.disconnect(reason)

        val translated = MiniMessage.miniMessage().stripTags(reason.content())
        History.createEntry(player.uniqueId, KickEntry(player.uniqueId, Timestamp.from(Instant.now()), translated))
    }

    override fun unban() {
        StaticQueryAdapter.builder()
            .query("DELETE FROM player_bans WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .delete()
            .send()
    }

    override fun unban(player: Player) {
        unban()

        StaticQueryAdapter.builder()
            .query("INSERT INTO player_unbans (uuid, executor_uuid) VALUES (?, ?);")
            .parameter { stmt ->
                stmt.setString(this.player.uniqueId.toString())
                stmt.setString(player.uniqueId.toString())
            }
            .insert()
            .send()
    }

    override fun unmute() {
        StaticQueryAdapter.builder()
            .query("DELETE FROM player_mutes WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .delete()
            .send()
    }

    override fun unmute(player: Player) {
        unmute()

        StaticQueryAdapter.builder()
            .query("INSERT INTO player_unmutes (uuid, executor_uuid) VALUES (?, ?);")
            .parameter { stmt ->
                stmt.setString(this.player.uniqueId.toString())
                stmt.setString(player.uniqueId.toString())
            }
            .insert()
            .send()
    }

    fun report(reason: ReportReason, reporterUUID: UUID, serverName: String, replayId: String?) {
        StaticQueryAdapter.builder()
            .query("INSERT INTO open_reports (uuid, reporter_uuid, reason, server, replay_id) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(player.uniqueId.toString())
                stmt.setString(reporterUUID.toString())
                stmt.setString(reason.name)
                stmt.setString(serverName)
                stmt.setString(replayId)
            }
            .insert()
            .send()
    }

    fun getBanEntry(): Optional<BanEntry> {
        return StaticQueryAdapter.builder(BanEntry::class.java)
            .query("SELECT * FROM player_bans WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .readRow { row ->
                val date = row.getTimestamp("date")
                val expires = row.getTimestamp("expires")
                val reason = row.getString("reason")
                val id = row.getString("ban_uid")

                BanEntry(player.uniqueId, date, expires, reason, id)
            }
            .firstSync()
    }

    fun getMuteEntry(): Optional<MuteEntry> {
        return StaticQueryAdapter.builder(MuteEntry::class.java)
            .query("SELECT * FROM player_mutes WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .readRow { row ->
                val date = row.getTimestamp("date")
                val expires = row.getTimestamp("expires")
                val reason = row.getString("reason")
                val id = row.getString("mute_uid")

                MuteEntry(player.uniqueId, date, expires, reason, id)
            }
            .firstSync()
    }

    fun name(): Optional<String> {
        return StaticQueryAdapter.builder(String::class.java)
            .query("SELECT name FROM players, player_bans WHERE players.uuid = player_bans.uuid AND players.uuid = ?;")
            .parameter { stmt -> stmt.setString(player.uniqueId.toString()) }
            .readRow { row -> row.getString("name") }
            .firstSync()
    }


}