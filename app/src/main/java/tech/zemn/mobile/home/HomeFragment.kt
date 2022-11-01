package tech.zemn.mobile.home

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tech.zemn.mobile.Constants
import tech.zemn.mobile.R
import tech.zemn.mobile.Screens
import tech.zemn.mobile.SharedViewModel
import tech.zemn.mobile.player.ZemnBroadcastReceiver
import tech.zemn.mobile.ui.theme.ZemnTheme

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var navController: NavController

    private val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = findNavController()
        requireActivity().window.apply {
            navigationBarColor = ContextCompat.getColor(requireContext(),R.color.primary)
        }
        return ComposeView(requireContext()).apply {
            setContent {
                ZemnTheme {
                    var currentScreen by rememberSaveable { mutableStateOf(Screens.AllSongs) }
                    val songs by viewModel.songs.collectAsState()
                    val allSongsListState = rememberLazyListState()
                    val currentSong by viewModel.currentSong.collectAsState()
                    val songPlaying by viewModel.currentSongPlaying.collectAsState()
                    val pendingPausePlayIntent = remember {
                        PendingIntent.getBroadcast(
                            context, ZemnBroadcastReceiver.PAUSE_PLAY_ACTION_REQUEST_CODE,
                            Intent(Constants.PACKAGE_NAME).putExtra(
                                ZemnBroadcastReceiver.AUDIO_CONTROL,
                                ZemnBroadcastReceiver.ZEMN_PLAYER_PAUSE_PLAY
                            ),
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    }
                    val albumsWithSongs by viewModel.albumsWithSongs.collectAsState()
                    val allAlbumsGridState = rememberLazyGridState()
                    val artistsWithSongs by viewModel.artistsWithSongs.collectAsState()
                    val allArtistsListState = rememberLazyListState()
                    var showMiniPlayer by remember { mutableStateOf(false) }
                    LaunchedEffect(key1 = currentSong, key2 = songPlaying) {
                        showMiniPlayer = currentSong != null && songPlaying != null
                    }
                    Scaffold(
                        topBar = {
                            HomeTopBar()
                        },
                        content = { paddingValues ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                HomeContent(
                                    currentScreen = currentScreen,
                                    onSongClicked = { index ->
                                        viewModel.setQueue(songs,index)
                                    },
                                    songs = songs,
                                    allSongsListState = allSongsListState,
                                    paddingValues = PaddingValues(
                                        bottom = paddingValues.calculateBottomPadding() + if (showMiniPlayer) 80.dp else 0.dp
                                    ),
                                    albumsWithSongs = albumsWithSongs,
                                    allAlbumsGridState = allAlbumsGridState,
                                    onAlbumClicked = { albumWithSongs ->
                                        viewModel.onAlbumClicked(albumWithSongs)
                                        navController.navigate(R.id.action_homeFragment_to_playlistFragment)
                                    },
                                    artistsWithSongs = artistsWithSongs,
                                    onArtistClicked = { artistWithSongs ->
                                        viewModel.onArtistClicked(artistWithSongs)
                                        navController.navigate(R.id.action_homeFragment_to_playlistFragment)
                                    },
                                    allArtistsListState = allArtistsListState,
                                    onSongFavouriteClicked = viewModel::changeFavouriteValue,
                                    currentSong = currentSong,
                                    onAddToQueueClicked = viewModel::addToQueue,
                                    onPlayAllClicked = {
                                        viewModel.setQueue(songs,0)
                                    },
                                    onShuffleClicked = {
                                        viewModel.shufflePlay(songs)
                                    }
                                )
                                if (showMiniPlayer) {
                                    MiniPlayer(
                                        showPlayButton = !songPlaying!!,
                                        onPausePlayPressed = {
                                            pendingPausePlayIntent.send()
                                        },
                                        song = currentSong!!,
                                        paddingValues = paddingValues,
                                        onMiniPlayerClicked = {
                                            navController.navigate(R.id.action_homeFragment_to_nowPlaying)
                                        }
                                    )
                                }
                            }
                        },
                        bottomBar = {
                            HomeBottomBar(
                                currentScreen = currentScreen,
                                onScreenChange = {
                                    currentScreen = it
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}