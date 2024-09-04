package theweeb.dev.chatbotbiometricauth.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity
data class Note(
    @PrimaryKey val noteId: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val responseType: String = ResponseType.USER.name,
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM HH:mm:ss")),
)

data class NoteTuple(
    @ColumnInfo(name = "noteId") val noteId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "responseType") val responseType: String,
    @ColumnInfo(name = "date") val date: String
)

@Serializable
data class NoteSerializable(
    val title: String = "",
    val content: String = "",
)

fun NoteSerializable.toNote(): Note {
    return Note(
        title = title,
        content = content
    )
}

fun NoteSerializable?.toNoteOrNull(): Note? {
    return if(this != null)
        Note(
            title = title,
            content = content
        )
    else
        null
}

suspend fun NoteSerializable.toJsonObject(): JSONObject = withContext(Dispatchers.IO){
    JSONObject().apply {
        put("note", this)
    }
}

fun JSONObject.toNoteSerializable(): NoteSerializable{
    return Json.decodeFromString<NoteSerializable>(Json.decodeFromString(toString()))
}