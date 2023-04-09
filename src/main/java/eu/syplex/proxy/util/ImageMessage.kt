package eu.syplex.proxy.util

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

class ImageMessage(image: BufferedImage, height: Int, imgChar: Char) {
    private val transparentChar = ' '
    private var lines: Array<String?>

    init {
        val chatColors: Array<Array<TextColor?>> = toChatColorArray(image, height)
        lines = toImgMessage(chatColors, imgChar)
    }

    //Append Text to Skull in JoinMe-Message
    fun appendText(vararg text: String): ImageMessage {
        for (y in lines.indices) {
            if (text.size > y) {
                lines[y] += " " + replaceColor(text[y])
            }
        }
        return this
    }

    private fun replaceColor(text: String): String {
        return text.replace("&0", "<#000000>")

            .replace("&1", "<#0000AA>")
            .replace("&2", "<#00AA00>")
            .replace("&3", "<#00AAAA>")
            .replace("&4", "<#AA0000>")
            .replace("&5", "<#AA00AA>")
            .replace("&6", "<#FFAA00>")
            .replace("&7", "<#AAAAAA>")
            .replace("&8", "<#555555>")
            .replace("&9", "<#5555FF>")
            .replace("&a", "<#55FF55>")
            .replace("&b", "<#55FFFF>")
            .replace("&c", "<#FF5555>")
            .replace("&d", "<#FF55FF>")
            .replace("&e", "<#FFFF55>")
            .replace("&f", "<#FFFFFF>")
    }

    fun appendText(vararg text: Component): ImageMessage {
        for (y in lines.indices) {
            if (text.size > y) {
                lines[y] += " " + text[y]
            }
        }
        return this
    }

    //Gets the Color of the Pixels in the Skin
    private fun toChatColorArray(image: BufferedImage, height: Int): Array<Array<TextColor?>> {
        val ratio = image.height.toDouble() / image.width
        val resized = resizeImage(image, (height / ratio).toInt(), height)
        val chatImg: Array<Array<TextColor?>> = Array(resized.width) { arrayOfNulls(resized.height) }
        for (x in 0 until resized.width) {
            for (y in 0 until resized.height) {
                val rgb = resized.getRGB(x, y)
                val closest: TextColor = TextColor.color(rgb)
                chatImg[x][y] = closest
            }
        }
        return chatImg
    }

    //adds Color and Char to the Face
    private fun toImgMessage(colors: Array<Array<TextColor?>>, imgChar: Char): Array<String?> {
        val lines = arrayOfNulls<String>(colors[0].size)
        for (y in colors[0].indices) {
            var line = ""
            for (x in colors.indices) {
                val color: TextColor? = colors[x][y]
                line += if (color != null) "<" + colors[x][y].toString() + ">" + imgChar + "</" + colors[x][y].toString() + ">" else transparentChar
            }
            lines[y] = line

        }
        return lines
    }

    //Resizes the Image of the Skull
    private fun resizeImage(originalImage: BufferedImage, width: Int, height: Int): BufferedImage {
        val af = AffineTransform()
        af.scale(
            width / originalImage.width.toDouble(),
            height / originalImage.height.toDouble()
        )
        val operation = AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
        return operation.filter(originalImage, null)
    }

    //Sends the Lines of Skull and Text to the Player
    fun sendToPlayer(player: Player) {
        for (line in lines) {
            if (line != null) {
                val mm = MiniMessage.miniMessage()
                player.sendMessage(mm.deserialize(line))
            }
        }
    }
}
