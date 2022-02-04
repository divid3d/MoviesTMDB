package com.example.moviesapp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    postfixColor: Color = MaterialTheme.colors.primary,
    minLines: Int = 3
) {
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember {
        mutableStateOf(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = White,
                        fontSize = 12.sp
                    )
                ) {
                    append(text)
                }
            }
        )
    }

    val postfixText = stringResource(if (isExpanded) R.string.show_less else R.string.show_more)

    val textLayoutResult = textLayoutResultState.value

    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                finalText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 12.sp
                        )
                    ) {
                        append(text)
                    }
                    withStyle(
                        style = SpanStyle(
                            color = postfixColor,
                            fontSize = 12.sp
                        )
                    ) {
                        append("  $postfixText")
                    }
                }
            }
            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult.getLineEnd(minLines - 1)
                val adjustedText = text
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(postfixText.length)
                    .dropLastWhile { it == ' ' || it == '.' }

                finalText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 12.sp
                        )
                    ) {
                        append("$adjustedText… ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = postfixColor,
                            fontSize = 12.sp
                        )
                    ) {
                        append(postfixText)
                    }
                }

                isClickable = true
            }
        }
    }

    Text(
        text = finalText,
        maxLines = if (isExpanded) Int.MAX_VALUE else minLines,
        onTextLayout = { textLayoutResultState.value = it },
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                enabled = isClickable,
                indication = null
            ) { isExpanded = !isExpanded }
            .animateContentSize(),
    )

}