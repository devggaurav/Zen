package com.github.pakka_papad.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.pakka_papad.data.music.Song
import com.github.pakka_papad.home.AllSongs

@Composable
fun PlaylistContent(
    paddingValues: PaddingValues,
    songs: List<Song>,
    songsListState: LazyListState,
    onSongClicked: (index: Int) -> Unit,
    onSongFavouriteClicked: (Song) -> Unit,
    currentSong: Song?,
    onAddToQueueClicked: (Song) -> Unit,
    onPlayAllClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onAddToPlaylistsClicked: (songLocation: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AllSongs(
            songs = songs,
            onSongClicked = onSongClicked,
            paddingValues = paddingValues,
            listState = songsListState,
            onFavouriteClicked = onSongFavouriteClicked,
            currentSong = currentSong,
            onAddToQueueClicked = onAddToQueueClicked,
            onPlayAllClicked = onPlayAllClicked,
            onShuffleClicked = onShuffleClicked,
            onAddToPlaylistsClicked = onAddToPlaylistsClicked,
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp)
                .background(Color.Black.copy(0.2f))
                .align(Alignment.BottomCenter)
        )
    }

}