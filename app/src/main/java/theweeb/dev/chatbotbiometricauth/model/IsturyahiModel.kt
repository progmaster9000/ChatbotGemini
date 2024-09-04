package theweeb.dev.chatbotbiometricauth.model

import android.util.Log
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.FunctionResponsePart
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.InvalidStateException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class IsturyahiModel(
    val model: GenerativeModel? = null,
    val chat: Chat? = null,
    val personality: ModelPersonality = ModelPersonality.DEFAULT
) {

    suspend fun sendMessage(content: Content): GenerateContentResponse {
        return chat!!.sendMessage(content)
    }

    fun removeNote(noteId: Int){

    }

    fun editNote(noteId: Int){

    }
}