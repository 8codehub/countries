package com.countries.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.countries.content.R as ContentR
import com.countries.ui.state.CountryDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailsScreen(
    countryId: String,
    onBack: () -> Unit,
    viewModel: CountryDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(countryId) {
        viewModel.load(countryId)
    }

    Scaffold(
        topBar = {
            DetailsTopBar(onBack = onBack)
        }
    ) { padding ->
        CountryDetailsBody(
            state = state,
            padding = padding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(ContentR.string.details_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Text(text = stringResource(ContentR.string.back_symbol))
            }
        }
    )
}

@Composable
private fun CountryDetailsBody(
    state: CountryDetailsUiState,
    padding: PaddingValues
) {
    when {
        state.isLoading -> {
            DetailsLoading(modifier = Modifier.padding(padding))
        }

        state.errorMessage != null -> {
            DetailsError(
                message = state.errorMessage,
                modifier = Modifier.padding(padding)
            )
        }

        else -> {
            CountryDetailsContent(
                state = state,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun DetailsLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DetailsError(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        modifier = modifier.padding(24.dp),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun CountryDetailsContent(
    state: CountryDetailsUiState,
    modifier: Modifier = Modifier
) {
    val details = state.uiCountryDetail

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AsyncImage(
            model = details.countryFlag,
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = details.countryName ?: stringResource(ContentR.string.placeholder_dash),
            style = MaterialTheme.typography.headlineSmall
        )

        DetailsRow(
            label = stringResource(ContentR.string.details_capital),
            value = details.capitalName ?: stringResource(ContentR.string.placeholder_dash)
        )

        DetailsRow(
            label = stringResource(ContentR.string.details_currencies),
            value = details.countryCurrencies.orEmpty().ifBlank {
                stringResource(ContentR.string.placeholder_dash)
            }
        )
    }
}

@Composable
private fun DetailsRow(
    label: String,
    value: String
) {
    Text(
        text = stringResource(ContentR.string.details_row_format, label, value),
        style = MaterialTheme.typography.bodyLarge
    )
}
