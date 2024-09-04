package theweeb.dev.chatbotbiometricauth.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import theweeb.dev.chatbotbiometricauth.model.Message

@Dao
interface ConversationDao {

    @Query("SELECT * FROM message ORDER BY date")
    fun getMessages(): Flow<List<Message>>

    @Query("DELETE FROM message")
    suspend fun clearMessages()

//    @Query("SELECT topic FROM conversation ORDER BY date DESC")
//    fun getConversationTopics(): List<String>

    @Upsert
    suspend fun sendMessage(message: Message)

//    @Delete
//    suspend fun deleteConversation(conversation: Conversation)
}