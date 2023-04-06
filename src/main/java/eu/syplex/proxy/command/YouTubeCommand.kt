package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import eu.syplex.proxy.util.URLHandler
import net.kyori.adventure.text.event.ClickEvent

class YouTubeCommand(private val translator: ComponentTranslator, private val urlHandler: URLHandler) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("messages", "no-player"))
            return
        }

        if (args.isNotEmpty()) {
            source.sendMessage(translator.fromConfigWithReplacement("messages", "invalid-usage", Placeholder.command, "youtube"))
            return
        }

        val message = translator.fromConfig("messages", "youtube")
            .append(translator.fromConfig("messages", "youtube-clickable")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, urlHandler.fromConfig("youtube"))))

        source.sendMessage(message)
    }
}