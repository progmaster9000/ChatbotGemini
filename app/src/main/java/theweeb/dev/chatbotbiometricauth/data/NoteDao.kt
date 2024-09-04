package theweeb.dev.chatbotbiometricauth.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import theweeb.dev.chatbotbiometricauth.model.Note
import theweeb.dev.chatbotbiometricauth.model.NoteTuple

@Dao
interface NoteDao {
    @Query("SELECT noteId, title, responseType ,date FROM note ORDER BY date DESC")
    fun getNoteTitles(): Flow<List<NoteTuple>>

    @Query("DELETE FROM Note WHERE noteId IN (:ids)")
    suspend fun deleteNotes(ids: List<String>)

    @Query("SELECT * FROM note WHERE noteId LIKE (:id)")
    suspend fun getNote(id: String): Note?

    @Upsert
    suspend fun upsertNote(note: Note)
}