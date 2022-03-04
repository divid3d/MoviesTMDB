package com.example.moviesapp.ui.components.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.moviesapp.R
import com.example.moviesapp.model.Review
import com.example.moviesapp.other.formatted
import com.example.moviesapp.ui.components.texts.ExpandableText
import com.example.moviesapp.ui.theme.White500
import com.example.moviesapp.ui.theme.spacing

@Composable
fun ReviewItem(
    modifier: Modifier,
    review: Review
) {
    val avatarPath = review.authorDetails.avatarPath

    val avatarUrl = when {
        avatarPath == null -> null
        avatarPath.startsWith("/") -> {
            avatarPath.removePrefix("/")
        }
        else -> review.authorDetails.avatarUrl
    }

    val score = review.authorDetails.rating

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary.copy(0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (avatarUrl != null) {
                    Box {
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = rememberImagePainter(
                                data = avatarUrl,
                                builder = {
                                    size(OriginalSize)
                                    scale(Scale.FILL)
                                    transformations(CircleCropTransformation())
                                    crossfade(true)
                                }
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = MaterialTheme.colors.surface, shape = CircleShape)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_outline_no_photography_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colors.primary.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = MaterialTheme.spacing.small)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = review.authorDetails.username,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    review.createdAt?.let { date ->
                        Text(
                            text = date.formatted(),
                            color = White500,
                            fontSize = 12.sp
                        )
                    }
                }

                score?.let { score ->
                    PresentableScoreItem(score = score)
                }
            }

            ExpandableText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium),
                text = review.content,
                minLines = 6
            )

            Column(modifier = Modifier.align(Alignment.End)) {
                review.updatedAt?.let { date ->
                    Text(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                        text = stringResource(
                            R.string.review_item_last_modified_text,
                            date.formatted()
                        ),
                        color = White500,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}