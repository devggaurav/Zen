package com.github.pakka_papad.nowplaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pakka_papad.components.SongCardV2
import com.github.pakka_papad.data.music.Song

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.Queue(
    queue: List<Song>,
    onSongClicked: (index: Int) -> Unit,
    onFavouriteClicked: (Song) -> Unit,
    currentSong: Song?,
    onDownArrowClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .align(Alignment.CenterHorizontally)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
            .asPaddingValues()
    ) {
        stickyHeader {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "down arrow icon",
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .size(36.dp)
                    .clickable(
                        onClick = onDownArrowClicked,
                        indication = rememberRipple(
                            bounded = true,
                            radius = 18.dp
                        ),
                        interactionSource = MutableInteractionSource()
                    ),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        itemsIndexed(
            items = queue,
            key = { index, song ->
                song.location
            }
        ) { index, song ->
            SongCardV2(
                song = song,
                onSongClicked = {
                    onSongClicked(index)
                },
                onFavouriteClicked = onFavouriteClicked,
                currentlyPlaying = (song.location == currentSong?.location)
            )
        }
    }
}