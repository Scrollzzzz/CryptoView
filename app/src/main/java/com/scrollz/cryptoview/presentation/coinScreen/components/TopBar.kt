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
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.scrollz.cryptoview.R

@Composable
@ExperimentalMaterial3Api
fun TopBar(
    isFavorite: Boolean,
    isNotificationOn: Boolean,
    onFavoriteClick: () -> Unit,
    showNotificationDialog: () -> Unit,
    popBackStack: () -> Unit
) {
    TopAppBar(
        title = {  },
        navigationIcon = {
            IconButton(
                onClick = popBackStack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.button_come_back),
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
                onClick = showNotificationDialog
            ) {
                Crossfade(
                    targetState = isNotificationOn,
                    animationSpec = tween(200)
                ) { isNotificationOn ->
                    if (isNotificationOn) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = stringResource(R.string.button_change_notification_settings),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    else {
                        Icon(
                            imageVector = Icons.Default.NotificationAdd,
                            contentDescription = stringResource(R.string.button_add_notification),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
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
                            contentDescription = stringResource(R.string.button_remove_from_favorites),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    else {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.button_add_to_favorites),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    )
}
