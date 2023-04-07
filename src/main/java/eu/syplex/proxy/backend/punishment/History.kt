package eu.syplex.proxy.backend.punishment

import eu.syplex.proxy.backend.database.sadu.StaticQueryAdapter
import eu.syplex.proxy.backend.punishment.impl.BanEntry
import eu.syplex.proxy.backend.punishment.impl.KickEntry
import eu.syplex.proxy.backend.punishment.impl.MuteEntry
import java.util.*

object History {

    private val histories = HashMap<UUID, LinkedList<Entry>>()

    fun createEntry(uuid: UUID, entry: Entry) {
        val entries = histories.getOrDefault(uuid, LinkedList())
        entries.add(entry)

        histories[uuid] = entries

        insertIntoDatabase(entry)
    }

    fun getEntries(uuid: UUID): LinkedList<Entry> {
        return histories.getOrDefault(uuid, LinkedList())
    }

    private fun insertIntoDatabase(entry: Entry) {
        if (entry !is BanEntry && entry !is MuteEntry && entry !is KickEntry) return

        StaticQueryAdapter.builder()
            .query("INSERT INTO player_history(uuid, date, expires, reason, type) VALUES (?, ?, ?, ?, ?);")
            .parameter { stmt ->
                stmt.setString(entry.uuid.toString())
                stmt.setTimestamp(entry.date)
                stmt.setFloat(entry.expires)
                stmt.setString(entry.reason)
                stmt.setInt(calcType(entry))
            }
            .insert()
            .send()
    }

    private fun calcType(entry: Entry): Int {
        return when (entry) {
            is BanEntry -> 1
            is MuteEntry -> 2
            else -> 0
        }
    }

}
