package theweeb.dev.chatbotbiometricauth.model

import theweeb.dev.chatbotbiometricauth.R

data class ModelPersonalityItem(
    val image: Int = 0,
    val modelName: String,
    val modelPersonality: ModelPersonality
){
    companion object{
        fun getPersonalities(): List<ModelPersonalityItem>{
            return listOf(
                ModelPersonalityItem(
                    image = ModelPersonality.DEFAULT.image,
                    modelName = ModelPersonality.DEFAULT.modelName,
                    modelPersonality = ModelPersonality.DEFAULT
                ),
                ModelPersonalityItem(
                    image = ModelPersonality.ANDREW_TATE.image,
                    modelName = ModelPersonality.ANDREW_TATE.modelName,
                    modelPersonality = ModelPersonality.ANDREW_TATE
                ),
                ModelPersonalityItem(
                    image = ModelPersonality.CHESS_PLAYER.image,
                    modelName = ModelPersonality.CHESS_PLAYER.modelName,
                    modelPersonality = ModelPersonality.CHESS_PLAYER
                ),
                ModelPersonalityItem(
                    image = ModelPersonality.TAGALOG.image,
                    modelName = ModelPersonality.TAGALOG.modelName,
                    modelPersonality = ModelPersonality.TAGALOG
                ),
                ModelPersonalityItem(
                    image = ModelPersonality.BISAYA.image,
                    modelName = ModelPersonality.BISAYA.modelName,
                    modelPersonality = ModelPersonality.BISAYA
                ),
                ModelPersonalityItem(
                    image = ModelPersonality.ANIME_GIRL.image,
                    modelName = ModelPersonality.ANIME_GIRL.modelName,
                    modelPersonality = ModelPersonality.ANIME_GIRL
                ),
                ModelPersonalityItem(
                    image = ModelPersonality.CHINITA_GIRL.image,
                    modelName = ModelPersonality.CHINITA_GIRL.modelName,
                    modelPersonality = ModelPersonality.CHINITA_GIRL
                )
            )
        }
    }
}

enum class ModelPersonality(
    val image: Int,
    val role: String,
    val modelName: String,
    val suggestedConversation: String,
) {
    DEFAULT(
        R.drawable.gemini,
        "The default gemini chatbot",
        "Gemini",
        "Good morning gemini"
    ),
    ANDREW_TATE (
        R.drawable.andrewtate,
        "You are Andrew Tate",
        "Andrew Tate",
        "What's up g?"
    ),
    CHESS_PLAYER (
        R.drawable.magnuscarlsen,
        "You know how to play chess like Magnus Carlsen. You must reply with the moves only.",
        "Magnus Carlsen",
        "e4"
    ),
    TAGALOG(
        R.drawable.rendon,
        "You speak Tagalog.",
        "Tagalog",
        "Magandang ugama sa iyo."
    ),
    BISAYA(
        R.drawable.mannypac,
        "You speak Bisaya like Manny Pacquiao and write words as it pronounced in their accent.",
        "Bisaya",
        "Maayong buntag."
    ),
    ANIME_GIRL(
        R.drawable.animegirl,
        "You speak like an anime girl",
        "Anime Girl",
        "Hi!"
    ),
    CHINITA_GIRL(
        R.drawable.chinitagirl,
        "You act like a pinay GIRLFRIEND that is a tsundere in personality. Refrain from using emojis a lot. Sometimes you say like 'HAHAHAH' to show emotions. Call me baby. Speak tagalog like a Conyo. Always add extra letters to your words to make it cutesy like 'hiiiii', 'miss youuuu.",
        "Short-haired Chinita Girlfriend",
        "Miss na kita!"
    )
}