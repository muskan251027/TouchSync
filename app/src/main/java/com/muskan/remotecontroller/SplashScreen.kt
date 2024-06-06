package com.muskan.remotecontroller

import android.window.SplashScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(navController: NavHostController) {
    var buttonVisible by remember { mutableStateOf(false) }

    // Animate the vertical position of the button
    val offsetY by animateFloatAsState(
        targetValue = if (buttonVisible) 0f else 100f,
        animationSpec = tween(durationMillis = 700, easing = LinearEasing)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF100c08),
                        Color(0xFF000036),
                        Color(0xFF003153)
                    )
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp)
                .scale(2f)
        )

        BlinkingConcentricCircles()

        Text(
            text = "Syncing Touch, Bridging Devices.",
            style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 0.dp)
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Apply the offsetY to the button's vertical position and add fade-in/out transition
        AnimatedVisibility(
            visible = buttonVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 2000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 2000))
        ) {
            Button(
                onClick = { navController.navigate("about_screen") },
                modifier = Modifier
                    .offset(y = offsetY.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(vertical = 15.dp, horizontal = 10.dp)
                    .animateContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007bb8)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Get started",
                        style = MaterialTheme.typography.bodyLarge
                            .copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    // Trigger the animation when the composable is first drawn
    LaunchedEffect(Unit) {
        delay(500) // Delay for a smooth transition
        buttonVisible = true
    }
}

@Composable
fun BlinkingConcentricCircles() {
    val transition = rememberInfiniteTransition()
    val color by transition.animateColor(
        initialValue = Color(0xFF00CCFF),
        targetValue = Color.White,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier.size(400.dp),
        onDraw = {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val minDimension = size.minDimension
            val firstRadius = minDimension * 0.24f // Adjust first circle radius
            val secondRadius = minDimension * 0.17f // Adjust second circle radius
            val thirdRadius = minDimension * 0.1f // Adjust third circle radius

            drawCircle(
                color = color,
                radius = firstRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 50f)
            )

            drawCircle(
                color = color,
                radius = secondRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 40f)
            )

            drawCircle(
                color = color,
                radius = thirdRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 30f)
            )
        }
    )
}


