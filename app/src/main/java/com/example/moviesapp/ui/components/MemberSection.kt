package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviesapp.model.Member
import com.example.moviesapp.ui.theme.spacing

@Composable
fun MemberSection(
    modifier: Modifier = Modifier,
    members: List<Member>,
    contentPadding: PaddingValues
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        contentPadding = contentPadding
    ) {
        items(members) { member ->
            MemberResultChip(
                modifier = Modifier.width(80.dp),
                member = member
            )
        }
    }
}