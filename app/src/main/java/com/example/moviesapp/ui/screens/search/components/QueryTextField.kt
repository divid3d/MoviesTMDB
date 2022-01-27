package com.example.moviesapp.ui.screens.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.moviesapp.R
import com.example.moviesapp.ui.theme.spacing

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QueryTextField(
    modifier: Modifier = Modifier,
    query: String?,
    loading: Boolean = false,
    showClearButton: Boolean = false,
    info: @Composable () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onQueryClear: () -> Unit = {},
    onVoiceSearchClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = query.orEmpty(),
            onValueChange = onQueryChange,
            placeholder = {
                Text(text = stringResource(R.string.search_placeholder))
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        enter = fadeIn(),
                        exit = fadeOut(),
                        visible = loading
                    ) {
                        CircularProgressIndicator()
                    }
                    Crossfade(
                        targetState = showClearButton
                    ) { show ->
                        if (show) {
                            IconButton(onClick = onQueryClear) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "clear"
                                )
                            }
                        } else {
                            IconButton(onClick = onVoiceSearchClick) {
                                Image(
                                    painter = painterResource(R.drawable.ic_baseline_keyboard_voice_24),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                                    contentDescription = "voice search"
                                )
                            }
                        }
                    }

                }
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus(force = true)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            singleLine = true
        )

        info()
    }

}