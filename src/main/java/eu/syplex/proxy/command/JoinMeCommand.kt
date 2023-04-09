package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.ImageMessage
import eu.syplex.proxy.util.JoinMeValidator
import eu.syplex.proxy.util.Placeholder
import net.kyori.adventure.text.Component
import java.net.URL
import javax.imageio.ImageIO


class JoinMeCommand(private val translator: ComponentTranslator, private val validator: JoinMeValidator) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val args = invocation.arguments()
        val sender = invocation.source()

        if (sender !is Player) { //Check if CommandSource is a Player
            sender.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if(!sender.hasPermission("proxy.command.joinme")) {
            sender.sendMessage(translator.fromConfig("no-permission"))
            return
        }

        if (args.isNotEmpty()) { //Check Command contains Arguments
            sender.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "joinme"))
            return
        }

        if (sender.currentServer.isEmpty) {
            sender.sendMessage(translator.fromConfig("joinme-error"))
            return
        }

        val connection = sender.currentServer.get()
        val img = ImageIO.read(URL("https://crafatar.com/avatars/" + sender.uniqueId.toString() + ".png")) //Read the Image from API-Server

        ImageMessage(img, 8, '\u2588').appendText(
            "", "", "",
            translator.rawWithTwoReplacements("joinme-message", Placeholder.player, Placeholder.server, sender.username, connection.serverInfo.name),
            "<click:run_command:/accept>" + translator.raw("joinme-clickable")
        ).sendToPlayer(sender)

        validator.createJoinMe(connection.server)
    }
}