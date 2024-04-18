package com.muskan.remotecontroller

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

// Define a custom shape for the buttons
val customButtonShape = RoundedCornerShape(
    topStart = CornerSize(0.dp),
    topEnd = CornerSize(0.dp),
    bottomStart = CornerSize(0.dp),
    bottomEnd = CornerSize(0.dp)
)

@Composable
fun MainScreen(navController: NavHostController) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main heading
        Text(
            text = "Enter IP Address",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 50.dp)
        )

        // BasicTextField with black border and shadow
        BasicTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
                .padding(vertical = 20.dp, horizontal = 12.dp)
        )

        // Button with brown color and more padding
        Button(
            onClick = {
                connect(navController, context, text.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            shape = customButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)) // Set background color
        ) {
            Text(
                "Start Remote Controller",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
    }
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
