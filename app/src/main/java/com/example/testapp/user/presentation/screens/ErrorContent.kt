package com.example.testapp.user.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.testapp.R
import com.example.testapp.common.theme.enterSpring

@Composable
fun ErrorContent(errorText: String?, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = !errorText.isNullOrEmpty(),
        enter = expandVertically(
            animationSpec = enterSpring(IntSize.VisibilityThreshold)
        ) + slideInVertically(
            animationSpec = enterSpring(IntOffset.VisibilityThreshold)
        )
    ) {
        Card(
            modifier = modifier
                .padding(12.dp, 8.dp, 12.dp, 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_error_dino),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurface)
                    .size(280.dp)
                    .scale(0.6f),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = errorText ?: stringResource(id = R.string.error_general),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ErrorContent(
            errorText = stringResource(id = R.string.error_general),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
