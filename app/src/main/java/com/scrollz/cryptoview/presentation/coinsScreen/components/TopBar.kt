package com.scrollz.cryptoview.presentation.coinsScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.presentation.common.CustomTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterial3Api
fun TopBar(
    isSearching: Boolean,
    searchText: String,
    scrollUp: () -> Unit,
    toggleSearch: () -> Unit,
    onSearchTextChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    if (isSearching) {
        SearchRow(
            focusRequester = focusRequester,
            searchText = searchText,
            scrollUp = scrollUp,
            toggleSearch = toggleSearch,
            onSearchTextChange = onSearchTextChange
        )
    }
    else {
        TopAppBar(
            title = {
                Text(
                    text = "CryptoView",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        toggleSearch()
                        scope.launch {
                            delay(100)
                            focusRequester.requestFocus()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
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
            )
        )
    }
}

@Composable
@ExperimentalMaterial3Api
fun SearchRow(
    focusRequester: FocusRequester,
    searchText: String,
    scrollUp: () -> Unit,
    toggleSearch: () -> Unit,
    onSearchTextChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 4.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = {
                onSearchTextChange("")
                scrollUp()
                toggleSearch()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Close search",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .focusRequester(focusRequester),
            value = searchText,
            onValueChange = { text ->
                onSearchTextChange(text)
                scrollUp()
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            shape = RoundedCornerShape(50),
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            placeholder = {
                Text(
                    text = "Search",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSearchTextChange("")
                        scrollUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            },
        )
    }
}
