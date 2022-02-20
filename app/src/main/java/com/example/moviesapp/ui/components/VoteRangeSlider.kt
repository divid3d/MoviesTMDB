package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.moviesapp.model.VoteRange
import com.example.moviesapp.other.singleDecimalPlaceFormatted

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VoteRangeSlider(
    modifier: Modifier = Modifier,
    voteRange: VoteRange,
    onCurrentVoteRangeChange: (ClosedFloatingPointRange<Float>) -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = voteRange.current.start.singleDecimalPlaceFormatted(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = voteRange.current.endInclusive.singleDecimalPlaceFormatted(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
        }

        RangeSlider(
            modifier = Modifier.fillMaxWidth(),
            values = voteRange.current,
            valueRange = voteRange.default,
            onValueChange = onCurrentVoteRangeChange
        )
    }
}