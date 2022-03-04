package com.example.moviesapp.ui.components.selectors

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.moviesapp.R
import com.example.moviesapp.other.createDateDialog
import com.example.moviesapp.other.formatted
import com.example.moviesapp.ui.theme.spacing
import java.util.*

@Composable
fun DateRangeSelector(
    modifier: Modifier = Modifier,
    fromDate: Date?,
    toDate: Date?,
    onFromDateChanged: (Date) -> Unit = {},
    onToDateChanged: (Date) -> Unit = {},
    onFromDateClearClicked: () -> Unit = {},
    onToDateClearClicked: () -> Unit = {}
) {
    val spacing = MaterialTheme.spacing.medium

    ConstraintLayout(modifier = modifier) {
        val (fromLabel, fromDateChip, arrowIcon, toLabel, toDateChip) = createRefs()

        Icon(
            modifier = Modifier.constrainAs(arrowIcon) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(fromDateChip.top)
                bottom.linkTo(fromDateChip.bottom)
            },
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )

        DateChip(
            modifier = Modifier
                .constrainAs(fromDateChip) {
                    top.linkTo(fromLabel.bottom)
                    linkTo(
                        start = parent.start,
                        end = arrowIcon.start,
                        bias = 0f,
                        endMargin = spacing
                    )
                    width = Dimension.fillToConstraints
                },
            initialDate = fromDate,
            maxDate = toDate,
            onDateChanged = onFromDateChanged,
            onClearClicked = onFromDateClearClicked
        )

        DateChip(
            modifier = Modifier
                .constrainAs(toDateChip) {
                    top.linkTo(toLabel.bottom)
                    linkTo(
                        start = arrowIcon.end,
                        end = parent.end,
                        bias = 1f,
                        startMargin = spacing
                    )
                    width = Dimension.fillToConstraints
                },
            initialDate = toDate,
            minDate = fromDate,
            onDateChanged = onToDateChanged,
            onClearClicked = onToDateClearClicked
        )

        Text(
            modifier = Modifier
                .constrainAs(fromLabel) {
                    top.linkTo(parent.top)
                    start.linkTo(fromDateChip.start)
                    bottom.linkTo(fromDateChip.top)
                },
            text = stringResource(R.string.date_range_selector_from_label),
            fontWeight = FontWeight.SemiBold
        )

        Text(
            modifier = Modifier
                .constrainAs(toLabel) {
                    top.linkTo(parent.top)
                    start.linkTo(toDateChip.start)
                    bottom.linkTo(toDateChip.top)
                },
            text = stringResource(R.string.date_range_selector_to_label),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DateChip(
    modifier: Modifier = Modifier,
    initialDate: Date?,
    minDate: Date? = null,
    maxDate: Date? = null,
    onDateChanged: (Date) -> Unit = {},
    onClearClicked: () -> Unit = {}
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clickable {
                createDateDialog(
                    context = context,
                    initialDate = initialDate,
                    onDateSelected = onDateChanged,
                    minDate = minDate,
                    maxDate = maxDate
                ).show()
            }
            .background(
                color = MaterialTheme.colors.primary.copy(0.5f),
                shape = MaterialTheme.shapes.small
            )
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_baseline_calendar_today_24),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
                modifier = Modifier.weight(1f),
                text = initialDate?.formatted()
                    ?: stringResource(R.string.date_range_selector_select_hint),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (initialDate != null) Color.White else Color.White.copy(0.5f)
            )
            AnimatedVisibility(
                visible = initialDate != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onClearClicked() },
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null
                )
            }
        }

    }
}