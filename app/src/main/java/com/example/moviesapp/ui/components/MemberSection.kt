package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.*
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
    title: String? = null,
    members: List<Member>,
    contentPadding: PaddingValues,
    onMemberClick: (Int) -> Unit = {}
) {
    Column(modifier = modifier) {
        title?.let { title ->
            SectionLabel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                text = title
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.small),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            contentPadding = contentPadding
        ) {
            items(members) { member ->
                MemberResultChip(
                    modifier = Modifier.width(80.dp),
                    member = member,
                    onClick = { onMemberClick(member.id) }
                )
            }
        }
    }
}