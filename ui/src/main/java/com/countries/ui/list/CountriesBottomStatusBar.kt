package com.countries.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.countries.content.R as ContentR

@Composable
fun CountriesBottomStatusBar(
    status: BottomStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        StatusText(
            text = statusText(status),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun StatusText(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun statusText(status: BottomStatus): String {
    return when (status) {
        BottomStatus.Online -> stringResource(ContentR.string.status_online)
        BottomStatus.NoInternet -> stringResource(ContentR.string.status_offline)
    }
}
