package com.muskan.remotecontroller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun AboutUsScreen(navController: NavHostController) {
    Box(
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
            )
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 0.dp)
                )
            }
            item {
                HeadingText()
            }
            item {
                Divider(
                    color = Color.LightGray,
                    thickness = 2.dp,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 15.dp)
                )
            }
            item {
                AboutUsText()
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate("main_screen") },
            contentColor = Color.White,
            containerColor = Color(0xFFF79256),
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 20.dp
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(
                text = "Start",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 20.dp)
            )
        }
    }
}

@Composable
fun HeadingText() {
    Text(
        text = "\"SYNCING TOUCH, ",
        style = MaterialTheme.typography.displayLarge.copy(
            fontSize = 60.sp,
            letterSpacing = 0.1.em,
        ),
        color = Color(0xFFF79256),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp, bottom = 0.dp).padding(horizontal = 15.dp)
    )
    Text(
        text = "BRIDGING DEVICES.\"",
        style = MaterialTheme.typography.displayLarge.copy(fontSize = 60.sp, letterSpacing = 0.1.em,),
        color = Color(0xFFFBD1A2),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 0.dp, bottom = 20.dp).padding(horizontal = 15.dp)
    )
}

@Composable
fun AboutUsText() {
    Text(
        text = """
            TouchSync is the brainchild of Muskan Aggarwal and Varun Bhatt, envisioned to redefine the way users interact with their devices. Our application bridges the gap between mobile and computer ecosystems, empowering users to seamlessly control their computers from the convenience of their mobile devices.

            With TouchSync, users have the flexibility to choose between Bluetooth and Wi-Fi connections, ensuring compatibility across various devices and networks. Whether you're an iOS or Android user, or prefer Mac or Windows systems, TouchSync offers a universal solution for remote device management.

            Driven by the latest advancements in technology, our application is built using Jetpack Compose and Java, leveraging the power of modern development tools to deliver a seamless user experience. From intuitive UI/UX design to robust functionality, Muskan Aggarwal and Varun Bhatt have meticulously crafted TouchSync to meet the evolving needs of our users.

            At TouchSync, we recognize the importance of bridging the gap between devices, enabling users to harness the full potential of their technology. As we continue to innovate and enhance our application, we remain committed to delivering cutting-edge solutions that empower users in their digital journeys.

            Copyright © Qubit, 2024
        """.trimIndent(),
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White,
        modifier = Modifier.padding(vertical = 30.dp, horizontal = 15.dp)
    )
}

