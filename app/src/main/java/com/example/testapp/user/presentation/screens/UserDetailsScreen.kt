package com.example.testapp.user.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.user.presentation.model.PictureUiModel
import com.example.testapp.user.presentation.model.UserDetailsUiState
import com.example.testapp.user.presentation.model.UserUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserDetailsScreen(
    uiState: UserDetailsUiState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.screen_user_detail_title),
                        style = MaterialTheme.typography.titleLarge,
                        overflow = Ellipsis,
                        maxLines = 1
                    )
                },
                modifier = Modifier.shadow(
                    elevation = 4.dp
                )
            )
        },
    ) { paddingValues ->
        when (uiState) {
            is UserDetailsUiState.UserUiState -> ScreenContent(
                uiState = uiState,
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            )

            is UserDetailsUiState.LoadingUiState -> {
                CenteredCircleLoader(uiState.isLoading)
            }

            is UserDetailsUiState.ErrorUiState -> {
                ErrorContent(
                    errorText = uiState.errorMessage,
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
                )
            }
        }
    }
}

@Composable
private fun ScreenContent(
    uiState: UserDetailsUiState.UserUiState,
    modifier: Modifier = Modifier,
) {

    val scrollState = rememberScrollState()
    var isDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {

                UserImage(
                    imageUrl = uiState.user.picture.medium,
                    onImageClick = { isDialogOpened = true }
                )

                if (isDialogOpened) {
                    LargeImageDialog(onClickOutside = { isDialogOpened = false }) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            model = uiState.user.picture.large,
                            contentDescription = null,
                            error = painterResource(R.drawable.ic_image_placeholder)
                        )
                    }
                }
                InfoFields(uiState)
            }
        }

        ZoomFab(
            modifier = Modifier.align(Alignment.TopEnd),
            onFabClicked = { isDialogOpened = true }
        )
    }
}


@Composable
private fun UserImage(
    imageUrl: String?,
    onImageClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier
                .clickable { onImageClick() }
                .padding(all = 16.dp)
                .size(250.dp)
                .clip(CircleShape),
            model = imageUrl,
            contentDescription = null,
            error = painterResource(R.drawable.ic_image_placeholder)
        )
    }
}


@Composable
private fun InfoFields(uiState: UserDetailsUiState.UserUiState) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        uiState.user.let {
            NameAndLocation(it)
            UserProperty(stringResource(R.string.timezone), it.timezone)
            UserProperty(stringResource(R.string.email), it.email)
            UserProperty(stringResource(R.string.phone), it.phone)
            UserProperty(stringResource(R.string.cell), it.cell)
            UserProperty(stringResource(R.string.gender), it.gender)
        }
    }
}

@Composable
private fun NameAndLocation(
    userData: UserUiModel
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(userData)
        Location(
            userData,
            modifier = Modifier
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
private fun Name(userData: UserUiModel?, modifier: Modifier = Modifier) {
    userData?.name?.let {
        Text(
            text = it,
            modifier = modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun Location(userData: UserUiModel, modifier: Modifier = Modifier) {
    Text(
        text = userData.location,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun UserProperty(label: String, value: String) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        HorizontalDivider()
        Text(
            text = label,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LargeImageDialog(onClickOutside: () -> Unit, content: @Composable () -> Unit) {
    Dialog(
        onDismissRequest = { onClickOutside() },
        DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClickOutside() }
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun ZoomFab(
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClicked,
        modifier = modifier
            .padding(16.dp)
            .height(48.dp)
            .widthIn(min = 48.dp),
        containerColor = MaterialTheme.colorScheme.onSurface
    ) {
        Icon(
            imageVector = Icons.Filled.ZoomIn,
            contentDescription = null
        )
    }
}

private val mockUser = UserUiModel(
    id = 1L,
    name = "John Doe",
    location = "New York",
    gender = "Unknown",
    picture = PictureUiModel(thumbnail = "", large = "", medium = ""),
    email = "test@gmail.com",
    phone = "555 55 55",
    cell = "+45 666 666 66",
    timezone = "Adelaide, Darwin",
    isFavorite = false
)

@Preview(showBackground = true)
@Composable
fun PreviewUserDetailsScreen() {
    UserDetailsScreen(
        uiState = UserDetailsUiState.UserUiState(mockUser)
    )
}