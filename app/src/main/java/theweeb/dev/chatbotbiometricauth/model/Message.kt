package theweeb.dev.chatbotbiometricauth.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.Part
import com.google.ai.client.generativeai.type.TextPart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity
data class Message(
    @PrimaryKey val messageId: String = UUID.randomUUID().toString(),
    val imageData: ByteArray? = null,
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM HH:mm:ss")),
    val content: String = "",
    val responseType: String = ResponseType.UNSPECIFIED.name
)

enum class ResponseType{
    USER,
    MODEL,
    UNSPECIFIED,
    ERROR
}

fun List<Message>.toChatContent(): List<Content> {

    val userPart = filter { it.responseType == ResponseType.USER.name }.map { it.content }
    val modelPart = filter { it.responseType == ResponseType.MODEL.name }.map { it.content }

    val content = map {
        Content(
            role = ResponseType.USER.name,
            parts = userPart.toTextPart()
        )
        Content(
            role = ResponseType.MODEL.name,
            parts = modelPart.toTextPart()
        )
    }

    return content
}

fun List<String>.toTextPart(): List<TextPart>{
    return map{
        TextPart(text = it)
    }
}