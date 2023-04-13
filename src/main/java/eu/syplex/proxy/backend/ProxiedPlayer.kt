package eu.syplex.proxy.backend

import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.backend.punishment.custructor.reason.BanReason
import eu.syplex.proxy.backend.punishment.custructor.reason.MuteReason
import java.util.*

interface ProxiedPlayer {

    fun exists(): Boolean

    fun ban(uid: UUID, reason: BanReason, id: String)

    fun mute(uid: UUID, reason: MuteReason, id: String)

    fun unban()

    fun unban(player: Player)

    fun unmute()

    fun unmute(player: Player)

    fun isBanned(): Boolean

    fun isMuted(): Boolean

}