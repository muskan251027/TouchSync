package com.muskan.remotecontroller

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

@Composable
fun MainScreen(navController: NavHostController) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

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
            )
//            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
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
                HeaderText()
            }
            item {
                Divider(
                    color = Color.LightGray,
                    thickness = 2.dp,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 15.dp, vertical = 40.dp)
                )
            }
            item {
                LimitedTextField(
                    value = text.text,
                    onValueChange = {
                        if (it.length <= 20) {
                            text = TextFieldValue(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 12.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                        .shadow(20.dp, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontSize = 25.sp)
                )
            }
        }
        Buttons(navController, context, text.text)
    }
}

@Composable
fun LimitedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None) // Disable imeAction
    )
}

@Composable
fun Buttons(navController: NavHostController, context: Context, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(
            onClick = { navController.navigate("about_screen") },
            modifier = Modifier
                .height(70.dp).weight(1f), // Fill available space
            shape = customButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF79256))
        ) {
            Text(
                text = "About",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),

                )
        }
        Button(
            onClick = { connect(navController, context, text) },
            modifier = Modifier
                .height(70.dp).weight(1f), // Fill available space
            shape = customButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007bb8))
        ) {
            Text(
                text = "Go",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),

                )
        }
    }
}
@Composable
fun HeaderText() {
    Text(
        text = "IP Address Information ",
        style = MaterialTheme.typography.displayLarge.copy(
            fontSize = 60.sp,
        ),
        color = Color(0xFFF79256),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 50.dp, bottom = 0.dp).padding(horizontal = 15.dp)
    )
}

fun connect(navController: NavHostController, context: android.content.Context, currentTextFieldValue: String) {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = currentTextFieldValue.trim()
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("0")
            val inputFromServer = DataInputStream(socket.getInputStream())
            val response = inputFromServer.readUTF()
            Log.d("response", "response: ${response}")
            if(response == "Success") {
                GlobalScope.launch(Dispatchers.Main) {
                    navController.navigate("another_Screen?text=$ipAddress")
                }
            } else {
                val text = "Wrong IP Address so cannot connect to server"
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "$text", Toast.LENGTH_SHORT).show()
                }
            }
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
