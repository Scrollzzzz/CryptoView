package com.scrollz.cryptoview.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    backgroundColor: Color,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        cursorBrush = SolidColor(textStyle.color),
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Row(
                modifier
                    .fillMaxSize()
                    .clip(shape)
                    .background(backgroundColor),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(6.dp))
                if (leadingIcon != null) {
                    leadingIcon()
                }
                Spacer(modifier = Modifier.width(6.dp))
                Row(
                    modifier
                        .fillMaxHeight()
                        .weight(1.0f, fill = true),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty() && placeholder != null) placeholder()
                        innerTextField()
                    }
                }
                if (value.isNotEmpty() && trailingIcon != null) trailingIcon()
            }
        },
    )
}