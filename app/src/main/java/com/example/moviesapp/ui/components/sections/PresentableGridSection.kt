package com.example.moviesapp.ui.components.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.PresentableItemState
import com.example.moviesapp.other.isScrollingTowardsStart
import com.example.moviesapp.other.items
import com.example.moviesapp.ui.components.buttons.ScrollToTop
import com.example.moviesapp.ui.components.items.PresentableItem
import com.example.moviesapp.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun PresentableGridSection(
    state: LazyPagingItems<out Presentable>,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    showRefreshLoading: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.default),
    scrollToBeginningItemsStart: Int = 30,
    onPresentableClick: (Int) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val isScrollingLeft = gridState.isScrollingTowardsStart()

    val showScrollToBeginningButton by derivedStateOf {
        val visibleMaxItem = gridState.firstVisibleItemIndex > scrollToBeginningItemsStart

        visibleMaxItem && isScrollingLeft
    }

    val onScrollToStartClick: () -> Unit = {
        coroutineScope.launch {
            gridState.animateScrollToItem(0)
        }
    }

    Box(modifier = modifier) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            contentPadding = contentPadding,
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            items(state) { presentable ->
                presentable?.let {
                    PresentableItem(
                        presentableState = PresentableItemState.Result(it),
                        onClick = { onPresentableClick(it.id) }
                    )
                }
            }

            state.apply {
                when {
                    loadState.refresh is LoadState.Loading && showRefreshLoading -> {
                        items(12) {
                            PresentableItem(presentableState = PresentableItemState.Loading)
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        items(3) {
                            PresentableItem(presentableState = PresentableItemState.Loading)
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = MaterialTheme.spacing.small, top = MaterialTheme.spacing.small),
            visible = showScrollToBeginningButton,
            enter = slideIn(
                animationSpec = tween(),
                initialOffset = { size -> IntOffset(size.width, 0) }),
            exit = fadeOut(animationSpec = spring()) + scaleOut(
                animationSpec = spring(),
                targetScale = 0.3f
            )
        ) {
            ScrollToTop(
                onClick = onScrollToStartClick
            )
        }
    }
}