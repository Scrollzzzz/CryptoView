package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@Composable
@ExperimentalMaterial3Api
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isFavorite: Boolean,
    onNotificationEnable: () -> Unit,
    onNotificationDisable: () -> Unit,
    onFavoriteClick: () -> Unit,
    popBackStack: () -> Unit
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {  },
        navigationIcon = {
            IconButton(
                onClick = popBackStack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Come back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        actions = {
            IconButton(
                onClick = onNotificationEnable
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationAdd,
                    contentDescription = "Add notification",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onNotificationDisable
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Add notification",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onFavoriteClick
            ) {
                Crossfade(
                    targetState = isFavorite,
                    animationSpec = tween(200)
                ) { isFavorite ->
                    if (isFavorite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Remove from favorites",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    else {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Add to favorites",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    )
}
