package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder
import net.kyori.adventure.text.event.ClickEvent

class YouTubeCommand(private val translator: ComponentTranslator) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (source !is Player) {
            source.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isNotEmpty()) {
            source.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "youtube"))
            return
        }

        val message = translator.fromConfig("youtube-non-clickable")
            .append(translator.fromConfig("youtube-clickable").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, translator.raw("youtube-requirements-url"))))

        source.sendMessage(message)
    }
}