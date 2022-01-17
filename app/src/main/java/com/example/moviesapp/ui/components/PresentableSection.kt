package com.example.moviesapp.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.moviesapp.R
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.isScrollingLeft
import com.example.moviesapp.ui.screens.movies.components.ScrollToStartButton
import com.example.moviesapp.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PresentableSection(
    modifier: Modifier = Modifier,
    title: String,
    state: LazyPagingItems<Presentable>,
    scrollToBeginningItemsStart: Int = 30,
    showMoreButton: Boolean = true,
    onMoreClick: () -> Unit = {},
    onPresentableClick: (Int) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isScrollingLeft = listState.isScrollingLeft()

    val showScrollToBeginningButton by derivedStateOf {
        val visibleMaxItem = listState.firstVisibleItemIndex > scrollToBeginningItemsStart

        visibleMaxItem && isScrollingLeft
    }

    val onScrollToStartClick: () -> Unit = {
        coroutineScope.launch {
            listState.animateScrollToItem(0)
        }
    }

    val isNotEmpty by derivedStateOf {
        state.run {
            loadState.refresh is LoadState.Loading || itemCount != 0
        }
    }

    if (isNotEmpty) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (showMoreButton) {
                    TextButton(onClick = onMoreClick) {
                        Text(
                            text = stringResource(R.string.movies_more),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
            Box {
                LazyRow(
                    state = listState,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                ) {
                    items(state) { movie ->
                        movie?.let {
                            PresentableItem(
                                //modifier = Modifier.animateItemPlacement(),
                                presentableState = PresentableState.Result(movie),
                                onClick = { onPresentableClick(it.id) }
                            )
                        }
                    }

                    state.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                items(10) {
                                    PresentableItem(presentableState = PresentableState.Loading)
                                }
                            }
                            loadState.append is LoadState.Loading -> {
                                item { PresentableItem(presentableState = PresentableState.Loading) }
                            }
//                            loadState.refresh is LoadState.Error -> {
//                                val e = state.loadState.refresh as LoadState.Error
//
//                                item { MovieItem(presentableState = PresentableState.Error) }
//                            }
//                            loadState.append is LoadState.Error -> {
//                                val e = state.loadState.refresh as LoadState.Error
//
//                                item { MovieItem(presentableState = PresentableState.Error) }
//                            }
                        }
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp),
                    visible = showScrollToBeginningButton,
                    enter = slideIn(
                        animationSpec = tween(),
                        initialOffset = { size -> IntOffset(-size.width, 0) }),
                    exit = slideOut(
                        animationSpec = tween(),
                        targetOffset = { size -> IntOffset(-size.width, 0) }),
                ) {
                    ScrollToStartButton(
                        onClick = onScrollToStartClick
                    )
                }
            }
        }
    }

}