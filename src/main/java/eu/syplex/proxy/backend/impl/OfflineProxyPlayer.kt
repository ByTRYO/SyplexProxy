package eu.syplex.proxy.backend.impl

import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.backend.ProxiedPlayer
import eu.syplex.proxy.backend.database.sadu.StaticQueryAdapter
import eu.syplex.proxy.backend.punishment.custructor.reason.BanReason
import eu.syplex.proxy.backend.punishment.custructor.reason.MuteReason
import eu.syplex.proxy.backend.punishment.entry.History
import eu.syplex.proxy.backend.punishment.entry.impl.BanEntry
import eu.syplex.proxy.backend.punishment.entry.impl.MuteEntry
import java.sql.Timestamp
import java.time.Instant
import java.util.*

class OfflineProxyPlayer(private val name: String) : ProxiedPlayer {

    private val uuid = UUID.fromString(getUUID())

    override fun exists(): Boolean {
        return StaticQueryAdapter.builder(Boolean::class.java)
            .query("SELECT uuid FROM players WHERE name=?;")
            .parameter { stmt -> stmt.setString(name) }
            .readRow { row -> row.getString("uuid") != null }
            .firstSync().orElse(false)
    }

    override fun ban(uid: UUID, reason: BanReason, id: String) {
        if (!assertUUIDNotNull()) return

        StaticQueryAdapter.builder()
            .query("INSERT INTO player_bans (uuid, executor_uuid, expires, reason, ban_uid) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(uuid.toString())
                stmt.setString(uid.toString())
                stmt.setTimestamp(reason.expires)
                stmt.setString(reason.name)
                stmt.setString(id)
            }
            .insert()
            .send()

        History.createEntry(uuid, BanEntry(uuid, Timestamp.from(Instant.now()), reason.expires, reason.name, id))
    }

    override fun mute(uid: UUID, reason: MuteReason, id: String) {
        if (!assertUUIDNotNull()) return

        StaticQueryAdapter.builder()
            .query("INSERT INTO player_mutes (uuid, executor_uuid, expires, reason, mute_uid) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(uuid.toString())
                stmt.setString(uid.toString())
                stmt.setTimestamp(reason.expires)
                stmt.setString(reason.name)
                stmt.setString(id)
            }
            .insert()
            .send()

        History.createEntry(uuid, MuteEntry(uuid, Timestamp.from(Instant.now()), reason.expires, reason.name, id))
    }

    override fun unban() {
        StaticQueryAdapter.builder()
            .query("DELETE FROM player_bans WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(uuid.toString()) }
            .delete()
            .send()
    }

    override fun unban(player: Player) {
        unban()

        StaticQueryAdapter.builder()
            .query("INSERT INTO player_unbans (uuid, executor_uuid) VALUES (?, ?);")
            .parameter { stmt ->
                stmt.setString(uuid.toString())
                stmt.setString(player.uniqueId.toString())
            }
            .insert()
            .send()
    }

    override fun unmute() {
        StaticQueryAdapter.builder()
            .query("DELETE FROM player_mutes WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(uuid.toString()) }
            .delete()
            .send()
    }

    override fun unmute(player: Player) {
        unmute()

        StaticQueryAdapter.builder()
            .query("INSERT INTO player_unmutes (uuid, executor_uuid) VALUES (?, ?);")
            .parameter { stmt ->
                stmt.setString(uuid.toString())
                stmt.setString(player.uniqueId.toString())
            }
            .insert()
            .send()
    }

    override fun isBanned(): Boolean {
        return StaticQueryAdapter.builder(Boolean::class.java)
            .query("SELECT uuid FROM player_bans WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(uuid.toString()) }
            .readRow { row -> row.getString("uuid") != null }
            .firstSync().orElse(false)
    }

    override fun isMuted(): Boolean {
        return StaticQueryAdapter.builder(Boolean::class.java)
            .query("SELECT uuid FROM player_mutes WHERE uuid=?;")
            .parameter { stmt -> stmt.setString(uuid.toString()) }
            .readRow { row -> row.getString("uuid") != null }
            .firstSync().orElse(false)
    }

    override fun countBans(): Optional<Int> {
        return StaticQueryAdapter.builder(Int::class.java)
            .query("SELECT COUNT(*) as ban_count FROM player_bans WHERE executor_uuid = ?;")
            .parameter { stmt -> stmt.setString(uuid.toString()) }
            .readRow { row -> row.getInt("ban_count") }
            .firstSync()

    }

    override fun countMutes(): Optional<Int> {
        return StaticQueryAdapter.builder(Int::class.java)
            .query("SELECT COUNT(*) as mute_count FROM player_mutes WHERE executor_uuid = ?;")
            .parameter { stmt -> stmt.setString(uuid.toString()) }
            .readRow { row -> row.getInt("mute_count") }
            .firstSync()
    }

    private fun assertUUIDNotNull(): Boolean {
        return uuid != null
    }

    private fun getUUID(): String? {
        return StaticQueryAdapter.builder(String::class.java)
            .query("SELECT uuid FROM players WHERE name=?;")
            .parameter { stmt -> stmt.setString(name) }
            .readRow { row -> row.getString("uuid") }
            .firstSync().orElse(UUID.randomUUID().toString())
    }

}