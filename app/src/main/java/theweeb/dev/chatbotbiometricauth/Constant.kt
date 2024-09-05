package theweeb.dev.chatbotbiometricauth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "model")

object Constant {
    val modelPersonality = stringPreferencesKey("model_personality")
    const val API_KEY = ""
}
