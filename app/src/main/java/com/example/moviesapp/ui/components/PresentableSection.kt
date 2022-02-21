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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.moviesapp.R
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.PresentableItemState
import com.example.moviesapp.other.isScrollingTowardsStart
import com.example.moviesapp.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PresentableSection(
    modifier: Modifier = Modifier,
    title: String,
    state: LazyPagingItems<out Presentable>,
    scrollToBeginningItemsStart: Int = 30,
    showMoreButton: Boolean = true,
    onMoreClick: () -> Unit = {},
    onPresentableClick: (Int) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isScrollingLeft = listState.isScrollingTowardsStart()

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
            loadState.refresh is LoadState.Loading || itemCount > 0
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
                SectionLabel(
                    modifier = Modifier.weight(1f),
                    text = title
                )
                if (showMoreButton) {
                    TextButton(onClick = onMoreClick) {
                        Text(
                            text = stringResource(R.string.movies_more),
                            color = Color.White,
                            fontSize = 12.sp
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                ) {
                    items(state) { movie ->
                        movie?.let {
                            PresentableItem(
                                //modifier = Modifier.animateItemPlacement(),
                                presentableState = PresentableItemState.Result(movie),
                                onClick = { onPresentableClick(it.id) }
                            )
                        }
                    }

                    state.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                items(10) {
                                    PresentableItem(presentableState = PresentableItemState.Loading)
                                }
                            }
                            loadState.append is LoadState.Loading -> {
                                item { PresentableItem(presentableState = PresentableItemState.Loading) }
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
                        .padding(start = MaterialTheme.spacing.small),
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