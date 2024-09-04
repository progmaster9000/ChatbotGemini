package theweeb.dev.chatbotbiometricauth.data

import androidx.room.Database
import androidx.room.RoomDatabase
import theweeb.dev.chatbotbiometricauth.model.Message
import theweeb.dev.chatbotbiometricauth.model.Note

@Database(
    version = 1,
    entities = [Message::class, Note::class]
)
abstract class IsturyahiDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun conversationDao(): ConversationDao
}