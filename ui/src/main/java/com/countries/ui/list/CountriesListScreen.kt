package com.countries.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.countries.ui.model.UiCountry
import com.countries.ui.navigation.NavigationRoute
import com.countries.ui.state.CountriesListUiState
import com.countries.content.R as ContentR

@Composable
fun CountriesListScreen(
    navigateTo: (NavigationRoute) -> Unit,
    viewModel: CountriesListViewModel = hiltViewModel()
) {
    /*
    * collect UI state with lifecycle awareness, use collectAsStateWithLifecycle in CountriesListScreen
    * */
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold {
        CountriesListLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            state = state,
            navigateTo = navigateTo,
            onQueryChange = viewModel::onQueryChanged
        )
    }

}

@Composable
private fun CountriesListLayout(
    modifier: Modifier,
    state: CountriesListUiState,
    navigateTo: (NavigationRoute) -> Unit,
    onQueryChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        CountriesListContent(
            state = state,
            onCountryClick = {
                navigateTo(NavigationRoute.CountryDetails(it.id))
            },
            onQueryChange = onQueryChange,
            modifier = Modifier.weight(1f)
        )

        CountriesBottomStatusBar(status = state.bottomStatus)
    }
}

@Composable
private fun CountriesListContent(
    state: CountriesListUiState,
    onCountryClick: (UiCountry) -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            CountriesSearchBar(
                query = state.query,
                onQueryChange = onQueryChange
            )

            state.errorMessage?.let {
                ErrorBanner(
                    message = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            CountriesListBody(
                state = state,
                onCountryClick = onCountryClick
            )
        }
    }
}

@Composable
private fun CountriesListBody(
    state: CountriesListUiState,
    onCountryClick: (UiCountry) -> Unit
) {
    when {
        state.isLoading && state.items.isEmpty() -> {
            CenterLoading(modifier = Modifier.fillMaxSize())
        }

        state.items.isEmpty() -> {
            EmptyState(modifier = Modifier.fillMaxSize())
        }

        else -> {
            CountriesList(
                items = state.items,
                onCountryClick = onCountryClick
            )
        }
    }
}

@Composable
private fun CountriesList(
    items: List<UiCountry>,
    onCountryClick: (UiCountry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { country ->
            CountryRow(
                item = country,
                onClick = { onCountryClick(country) },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CountryRow(
    item: UiCountry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CountryFlag(
                name = item.name,
                url = item.flagUrl
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CountryFlag(
    name: String,
    url: String?
) {
    val painter = rememberAsyncImagePainter(
        model = url,
        placeholder = painterResource(com.countries.ui.R.drawable.placeholder),
        error = painterResource(com.countries.ui.R.drawable.placeholder)
    )

    Image(
        painter = painter,
        contentDescription = stringResource(
            ContentR.string.flag_content_description,
            name
        ),
        modifier = Modifier.size(48.dp),
        contentScale = ContentScale.Crop
    )
}


@Composable
private fun CountriesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        label = { Text(text = stringResource(ContentR.string.search_countries)) },
        singleLine = true
    )
}

@Composable
private fun CenterLoading(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(ContentR.string.empty_countries),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
