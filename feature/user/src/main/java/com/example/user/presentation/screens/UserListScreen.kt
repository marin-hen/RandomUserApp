package com.example.user.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.feature.user.R
import com.example.user.presentation.model.ErrorUiMessage
import com.example.user.presentation.model.PictureUiModel
import com.example.user.presentation.model.UserListUiState
import com.example.user.presentation.model.UserUiModel
import kotlinx.collections.immutable.persistentListOf

val filledHeartIcon: ImageVector = Icons.Filled.Favorite
val unfilledHeartIcon: ImageVector = Icons.Outlined.FavoriteBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserListScreen(
    state: UserListUiState,
    error: ErrorUiMessage,
    onItemDetailsClick: (Long) -> Unit,
    onFavoriteUserClick: (Long, Boolean) -> Unit,
    onFavoriteFilterClick: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onClearError: () -> Unit
) {
    val filterFilledHeartIcon = ImageVector.vectorResource(id = R.drawable.ic_favorite_filled)
    val filterUnfilledHeartIcon = ImageVector.vectorResource(id = R.drawable.ic_favorite)
    var isHeartFilledState by remember { mutableStateOf(state.isFavoriteUsersEnabled) }
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                SnackBarWithIcon(data)
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.screen_user_list_title),
                        style = MaterialTheme.typography.titleLarge,
                        overflow = Ellipsis,
                        maxLines = 1
                    )
                },
                modifier = Modifier.shadow(
                    elevation = 4.dp
                ),
                actions = {
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .testTag(stringResource(id = R.string.test_tag_favorite_toggle)),
                        onClick = {
                            isHeartFilledState = !isHeartFilledState
                            onFavoriteFilterClick(isHeartFilledState)
                        }
                    ) {
                        Icon(
                            imageVector = if (isHeartFilledState) filterFilledHeartIcon else filterUnfilledHeartIcon,
                            contentDescription = null,
                            tint = if (isHeartFilledState) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        ScreenContent(
            uiState = state,
            onItemDetailsClick = onItemDetailsClick,
            onFavoriteClick = onFavoriteUserClick,
            onRefresh = onRefresh,
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .navigationBarsPadding()
        )

        error.errorMessage?.let {
            val errorMsg = stringResource(id = it)
            LaunchedEffect(it) {
                snackBarHostState.showSnackbar(
                    message = errorMsg,
                    duration = SnackbarDuration.Long,
                )
                onClearError.invoke()
            }
        }
    }
}

@Composable
private fun ScreenContent(
    uiState: UserListUiState,
    onItemDetailsClick: (Long) -> Unit,
    onFavoriteClick: (Long, Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("TAG", "ScreenContent:CenteredCircleLoader ${uiState.isLoading} ")
    CenteredCircleLoader(visible = uiState.isLoading)
    UserList(
        uiState,
        onItemDetailsClick,
        onFavoriteClick,
        onRefresh,
        modifier
    )
    ErrorContent(
        errorText = uiState.errorMessage,
        modifier = modifier
    )
}

@Composable
fun CenteredCircleLoader(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircleLoader(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(100.dp)
                    .testTag(stringResource(id = R.string.test_tag_loading)),
            )
        }
    }
}

@Composable
fun SnackBarWithIcon(data: SnackbarData) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = data::dismiss)
            ) {
                Image(
                    modifier = Modifier.padding(end = 16.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface),
                    painter = painterResource(id = R.drawable.ic_error_dino),
                    contentDescription = null,
                )
                Text(text = data.visuals.message)
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserList(
    state: UserListUiState,
    onItemDetailsClick: (Long) -> Unit,
    onFavoriteClick: (Long, Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onRefresh() },
        modifier = modifier
            .fillMaxSize()
            .testTag(stringResource(id = R.string.test_tag_user_list))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            state = rememberLazyListState()
        ) {
            itemsIndexed(
                items = state.users,
                // todo       key = { _, user -> user.id}
            ) { index, user ->
                if (index > 0) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), // Customize color and transparency
                        thickness = 1.dp,
                        modifier = Modifier.padding(start = 16.dp) // Add padding if needed
                    )
                }
                UserItem(
                    item = user,
                    onItemDetailsClick = onItemDetailsClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun UserItem(
    item: UserUiModel,
    onItemDetailsClick: (Long) -> Unit,
    onFavoriteClick: (Long, Boolean) -> Unit
) {
    val onFavoriteClickProvider = remember(item.id) {
        { isFavorite: Boolean ->
            onFavoriteClick(item.id, isFavorite)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { onItemDetailsClick(item.id) })
            .testTag(stringResource(id = R.string.test_tag_user_item_with_text)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .size(72.dp)
                .clip(CircleShape),
            model = item.picture.thumbnail,
            contentDescription = null,
            error = painterResource(R.drawable.ic_image_placeholder)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = Ellipsis
            )

            Text(
                text = item.location,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = Ellipsis
            )
        }

        IconToggleButton(
            modifier = Modifier.padding(end = 16.dp),
            checked = item.isFavorite,
            onCheckedChange = onFavoriteClickProvider
        ) {
            Icon(
                imageVector = if (item.isFavorite) filledHeartIcon else unfilledHeartIcon,
                contentDescription = "Favorite Filter",
                tint = if (item.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }
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

val mockUserListState = UserListUiState(
    users = persistentListOf(mockUser, mockUser.copy(name = "Gina Doe"))
)

@Preview(showBackground = true)
@Composable
fun PreviewUserListScreen() {
    UserListScreen(
        state = mockUserListState,
        error = ErrorUiMessage(errorMessage = null),
        onItemDetailsClick = {},
        onFavoriteUserClick = { _, _ -> },
        onFavoriteFilterClick = {},
        onRefresh = {},
        onClearError = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUserItem() {
    UserItem(
        item = mockUser,
        onItemDetailsClick = {},
        onFavoriteClick = { _, _ -> }
    )
}