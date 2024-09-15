package com.example.notesapp

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

class Repository {

    private val supabase = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }

    suspend fun getNotes(): List<Note> {
        return supabase.from(DATABASE).select().decodeList<Note>()
    }

    suspend fun addNote(newNote: String): Note {
        return supabase.from(DATABASE).insert(mapOf(BODY to newNote)) {
            select()
            single()
        }.decodeAs<Note>()
    }

    suspend fun deleteNote(noteId: Int) {
        supabase.from(DATABASE).delete {
            filter {
                eq("id", noteId)
            }
        }
    }


    companion object {
        private const val DATABASE = "notes"
        private const val BODY = "body"
        private const val SUPABASE_URL = "https://swqvleedzdrojyrspkbu.supabase.co"
        private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InN3cXZsZWVkemRyb2p5cnNwa2J1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjYzOTQ3NDcsImV4cCI6MjA0MTk3MDc0N30.WZnKB6R_rkEbd9l1zMVWJhzIqG64l5ufrQslnQDcdqk"


    }
}