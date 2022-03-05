package com.example.moviesapp.ui.screens.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.PopupProperties
import com.example.moviesapp.R
import com.example.moviesapp.other.partiallyAnnotatedString
import com.example.moviesapp.ui.theme.spacing

@Composable
fun QueryTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    query: String?,
    suggestions: List<String> = emptyList(),
    loading: Boolean = false,
    showClearButton: Boolean = false,
    voiceSearchAvailable: Boolean = false,
    info: @Composable () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onQueryClear: () -> Unit = {},
    onKeyboardSearchClicked: (KeyboardActionScope.() -> Unit)? = null,
    onVoiceSearchClick: () -> Unit = {},
    onSuggestionClick: (String) -> Unit = {}
) {
    var hasFocus by remember { mutableStateOf(false) }

    val suggestionsExpanded by derivedStateOf {
        hasFocus && suggestions.isNotEmpty()
    }

    Column(
        modifier = modifier.onFocusChanged { focusState ->
            hasFocus = focusState.hasFocus
        },
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                                if (voiceSearchAvailable) {
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

                    }
                },
                keyboardActions = KeyboardActions(
                    onSearch = onKeyboardSearchClicked
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                singleLine = true
            )

            SuggestionsDropdown(
                modifier = Modifier.fillMaxWidth(),
                expanded = suggestionsExpanded,
                query = query,
                suggestions = suggestions,
                onSuggestionClick = onSuggestionClick
            )
        }

        info()
    }

}

@Composable
private fun SuggestionsDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    query: String?,
    suggestions: List<String> = emptyList(),
    onSuggestionClick: (String) -> Unit = {}
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = {},
        properties = PopupProperties(
            focusable = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        suggestions.map { suggestion ->
            DropdownMenuItem(
                onClick = { onSuggestionClick(suggestion) }
            ) {
                val annotatedString = partiallyAnnotatedString(
                    text = suggestion,
                    content = query.orEmpty()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_baseline_history_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MaterialTheme.spacing.small),
                        text = annotatedString,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}