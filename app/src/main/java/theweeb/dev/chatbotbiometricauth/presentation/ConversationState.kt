package theweeb.dev.chatbotbiometricauth.presentation

import android.util.Log
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.FunctionCallingConfig
import com.google.ai.client.generativeai.type.FunctionDeclaration
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.Tool
import com.google.ai.client.generativeai.type.ToolConfig
import com.google.ai.client.generativeai.type.defineFunction
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import theweeb.dev.chatbotbiometricauth.Constant
import theweeb.dev.chatbotbiometricauth.model.IsturyahiModel
import theweeb.dev.chatbotbiometricauth.model.Message
import theweeb.dev.chatbotbiometricauth.model.ModelPersonality
import theweeb.dev.chatbotbiometricauth.model.NoteSerializable
import theweeb.dev.chatbotbiometricauth.model.NoteTuple
import theweeb.dev.chatbotbiometricauth.model.toChatContent
import theweeb.dev.chatbotbiometricauth.model.toNoteSerializable

data class ConversationState(
    val model: IsturyahiModel? = null,
    var messageCollectionJob: Job? = null,
    val conversationMessages: List<Message> = emptyList(),
    val modelNoteResponse: String = "",
    val noteTitles: List<NoteTuple> = emptyList(),
    var currentMessage: String = "",
    val isConversationLoading: Boolean = true
){
    fun createModel(personality: ModelPersonality): IsturyahiModel{
        val functions = listOf(
            defineFunction(
                name = "createNote",
                description = "create a note with a title and a content based from the given prompt",
                Schema.str(name = "title", description = "title of the note"),
                Schema.str(name = "content", description = "content of the note")
            ){ title, content ->
                createNote(title = title, content = content)
            },
//            defineFunction(
//                name = "sendRandomMessage",
//                description = "send message",
//                Schema.str(
//                    name = "content",
//                    description = "content for you to receive and respond with",
//                ),
//            ){ message ->
//                Log.d("messageFromModel", message)
//                sendRandomMessage(content = message)
//            },
//            defineFunction(
//                name = "createNoteObject",
//                description = "create a note with a title and a content, your response should be in JSON.",
//                Schema(
//                    name = "note",
//                    description = "this is the note object",
//                    properties = mapOf(
//                        "title" to Schema.str("title", "title of the note"),
//                        "content" to Schema.str("content", "content of the note"),
//                    ),
//                    required = listOf(
//                        "title",
//                        "content"
//                    ),
//                    type = FunctionType.OBJECT
//                )
//            ){ note ->
//                val noteObject = Json.decodeFromString<NoteSerializable>(note.toString())
//                createNoteObject(noteObject)
//            }
        )
        val tools = listOf(
            Tool(
                functionDeclarations = functions
            )
        )
        val model = GenerativeModel(
            "gemini-1.5-flash-exp-0827",
            Constant.API_KEY,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 500
                topP = 1f
                maxOutputTokens = 1000
            },
            safetySettings = listOf(
                SafetySetting(
                    harmCategory = HarmCategory.SEXUALLY_EXPLICIT,
                    threshold = BlockThreshold.NONE
                ),
                SafetySetting(
                    harmCategory = HarmCategory.HARASSMENT,
                    threshold = BlockThreshold.NONE
                ),
                SafetySetting(
                    harmCategory = HarmCategory.HATE_SPEECH,
                    threshold = BlockThreshold.NONE
                ),
                SafetySetting(
                    harmCategory = HarmCategory.DANGEROUS_CONTENT,
                    threshold = BlockThreshold.NONE
                ),
            ),
            toolConfig = ToolConfig(
                functionCallingConfig = FunctionCallingConfig(
                    mode = FunctionCallingConfig.Mode.AUTO
                )
            ),
            tools = tools,
            systemInstruction = Content.Builder().text(personality.role).build()
        )

        val chat = model.startChat(
            history = conversationMessages.toChatContent()
        )

        return IsturyahiModel(model = model, personality = personality, chat = chat)
    }

    private suspend fun createNote(title: String, content: String): JSONObject = withContext(Dispatchers.IO) {
        JSONObject().apply {
            put("title", title)
            put("content", content)
        }
    }

    private suspend fun sendRandomMessage(content: String): JSONObject = withContext(Dispatchers.IO) {
        JSONObject().apply {
            put("content", content)
        }
    }

    fun getScaffoldTitle(): String {
        return model?.personality?.modelName ?: "MyChatBot"
    }
}