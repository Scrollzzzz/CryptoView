package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.R

@Composable
@ExperimentalMaterial3Api
fun NotificationDialog(
    isNotificationDialogVisible: Boolean,
    isNotificationOn: Boolean,
    timePickerState: TimePickerState,
    closeDialog: () -> Unit,
    enableNotification: () -> Unit,
    disableNotification: () -> Unit
) {
    val scrollState = rememberScrollState()

    if (isNotificationDialogVisible) {
        AlertDialog(
            onDismissRequest = closeDialog
        ) {
            Surface(
                shape = RoundedCornerShape(35.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.notification_dialog_header),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TimePicker(
                        state = timePickerState,
                        layoutType = TimePickerLayoutType.Vertical,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = MaterialTheme.colorScheme.surface,
                            clockDialSelectedContentColor = MaterialTheme.colorScheme.background,
                            clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSecondary,
                            selectorColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = MaterialTheme.colorScheme.background,
                            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary,
                            timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onBackground,
                            timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier
                                .widthIn(max = 96.dp)
                                .weight(1.0f, fill = true),
                            onClick = {
                                closeDialog()
                                enableNotification()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground,
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 6.dp),
                                text = if (isNotificationOn) stringResource(R.string.notification_dialog_update)
                                        else stringResource(R.string.notification_dialog_enable),
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                        if (isNotificationOn) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                modifier = Modifier
                                    .widthIn(max = 96.dp)
                                    .weight(1.0f, fill = true),
                                onClick = {
                                    closeDialog()
                                    disableNotification()
                                },
                                border = BorderStroke(
                                    width = 1.dp,
                                    brush = SolidColor(MaterialTheme.colorScheme.onBackground)
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    text = stringResource(R.string.notification_dialog_disable),
                                    style = MaterialTheme.typography.displayMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
