// ui/feed/FeedViewModel.kt
package com.example.memestreamproto.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.memestreamproto.data.MemeRepository
import kotlinx.coroutines.launch

class FeedViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MemeRepository(app)

    val memes = repo.observeAll().asLiveData()

    fun refresh(userId: String?) = viewModelScope.launch {
        repo.refreshFromServer(userId)
    }

    fun addDummy(userId: String) = viewModelScope.launch {
        repo.createLocalMeme(
            userId = userId,
            title = "Offline test " + System.currentTimeMillis(),
            url = "https://media.giphy.com/media/3o7abxtmPxanzaESGY/giphy.gif",
            tags = listOf("test","offline")
        )
    }

    fun pushPending() = viewModelScope.launch {
        repo.pushPending()
    }
}
