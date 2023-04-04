package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor

class YouTubeCommand : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(Component.text("Dazu musst du ein Spieler sein!").color(NamedTextColor.RED))
            return
        }

        if (args.isNotEmpty()) {
            source.sendMessage(Component.text("Bitte benutze ").color(NamedTextColor.RED).append(Component.text("/youtube").color(NamedTextColor.GOLD)))
            return
        }

        val message = Component.text("Unsere Anforderungen an YouTuber findet du").color(NamedTextColor.GRAY)
            .append(Component.text("[Hier]")
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://www.zdf.de/nachrichten/politik/ukraine-russland-konflikt-blog-100.html")))

        source.sendMessage(message)

    }
}