package com.example.moviesapp.ui.screens.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.CreditsPresentable
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.ui.components.texts.SectionLabel
import com.example.moviesapp.ui.theme.spacing


@Composable
fun CreditsList(
    modifier: Modifier = Modifier,
    title: String,
    credits: List<CreditsPresentable>,
    onCreditsClick: (MediaType, Int) -> Unit = { _, _ -> }
) {
    val creditsGroups = credits.groupBy { credit -> credit.id }.toList()

    if (credits.isNotEmpty()) {
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
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
            ) {
                items(creditsGroups) { (id, credits) ->
                    val posterPath = credits.firstNotNullOfOrNull { member -> member.posterPath }
                    val mediaType = credits.firstOrNull()?.mediaType
                    val mediaTitle = credits.firstNotNullOfOrNull { member -> member.title }
                    val infoText = credits.mapNotNull { member ->
                        member.infoText
                    }.joinToString(separator = ", ")

                    CreditsItem(
                        posterPath = posterPath,
                        title = mediaTitle,
                        infoText = infoText,
                        onClick = {
                            mediaType?.let { type ->
                                onCreditsClick(type, id)
                            }
                        }
                    )
                }
            }
        }
    }

}