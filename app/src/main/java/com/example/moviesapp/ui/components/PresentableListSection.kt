package com.example.moviesapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.PresentableItemState
import com.example.moviesapp.ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PresentableListSection(
    modifier: Modifier = Modifier,
    title: String,
    list: List<Presentable>,
    selectedId: Int? = null,
    onPresentableClick: (Int) -> Unit = {}
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(selectedId) {
        val itemIndex = list.indexOfFirst { presentable -> presentable.id == selectedId }

        if (itemIndex >= 0) {
            lazyListState.scrollToItem(itemIndex)
        }
    }

    if (list.isNotEmpty()) {
        Column(modifier = modifier) {
            SectionLabel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.medium),
                text = title
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.small),
                state = lazyListState,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
            ) {
                items(list) { presentable ->
                    PresentableItem(
                        presentableState = PresentableItemState.Result(presentable),
                        selected = selectedId == presentable.id,
                        showScore = false,
                        onClick = { onPresentableClick(presentable.id) }
                    )
                }
            }
        }
    }

}