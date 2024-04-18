package com.muskan.remotecontroller

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.Socket

@Composable
fun SecondScreen(navController: NavHostController, text: String) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isPopupVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


    //val navBackStackEntry by navController.currentBackStackEntryAsState()
    //val ipAddress = navBackStackEntry?.arguments?.getString("text") ?: ""
    //val ipAddress = "10.0.0.63"
    val ipAddress = text.trim()


    println("ipAddress: $ipAddress")

    //val ipAddress = navBackStackEntry.arguments?.getString("text") ?: ""




    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .height(50.dp) // Fixed height for all buttons
                    .weight(1f), // Fill available space
                shape = customButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
            ) {
                Text("Shortcuts")
            }
            if (showDialog) {
                /*AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Popup Title") },
                    text = { Text(text = "Popup message.") },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text(text = "OK")
                        }
                    }
                )*/
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Popup Title") },
                    text = { Text(text = "Popup message.") },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text(text = "OK")
                        }
                    }
                )
            }

            Button(
                onClick = { screenshot(ipAddress) },
                modifier = Modifier
                    .height(50.dp) // Fixed height for all buttons
                    .weight(1f), // Fill available space
                shape = customButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
            ) {
                Text("Screenshot")
            }
            Button(
                onClick = { showKeyboard(context, ipAddress) },
                modifier = Modifier
                    .height(50.dp) // Fixed height for all buttons
                    .weight(1f), // Fill available space
                shape = customButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
            ) {
                Text("Keyboard")
            }


        }
        Box(
            modifier = Modifier
                .weight(1f)
                //.height(100.dp) // Set the height to be small
                .background(Color.Transparent),
            //.aspectRatio(1f),
            contentAlignment = Alignment.Center

        ) {
            TouchView(
                onTouch = { deltaX, deltaY ->
                    println("DeltaX: $deltaX, DeltaY: $deltaY")
                    // Send the coordinates to the server
                    sendCoordinatesToServer(deltaX, deltaY, ipAddress)
                },
                ipAddress


            )
        }
        Box(
            modifier = Modifier
                //.weight(1f)
                .height(100.dp) // Set the height to be small
                .background(Color.Transparent),
            //.aspectRatio(1f),
            contentAlignment = Alignment.Center

        ) {
            TouchView2(
                onTap = {


                },
                onDoubleTap = {


                    Log.d("onDoubleTap", "onDoubleTap")
                },
                ipAddress,


                )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start, // Align buttons to start and end
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left click button
                Button(
                    onClick = { leftClick(ipAddress) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = customButtonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                ) {
                    Text("Left Click")
                }

                // Scroll up, scroll down buttons arranged in a column
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                ) {
                    Button(
                        onClick = { scrollUp(ipAddress) },
                        modifier = Modifier
                            .fillMaxWidth().height(IntrinsicSize.Max).weight(1f),
                        shape = customButtonShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                    ) {
                        Text("Scroll Up")
                    }
                    Button(
                        onClick = { scrollDown(ipAddress) },
                        modifier = Modifier
                            .fillMaxWidth().height(IntrinsicSize.Max).weight(1f),
                        shape = customButtonShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                    ) {
                        Text("Scroll Down")
                    }
                }

                // Right click button
                Button(
                    onClick = { rightClick(ipAddress) },
                    modifier = Modifier
                        .weight(1f).fillMaxHeight(),
                    shape = customButtonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                ) {
                    Text("Right Click")
                }
            }
        }



    }
}

@Composable
fun TouchView2(onTap: () -> Unit, onDoubleTap: () -> Int, ipAddress: String) {
    var doubleTapCount by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(192, 153, 153))
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {

                        Log.d("onDoubleTap", "onDoubleTap")

                        doubleTap(ipAddress)
                    },
                    onTap = {

                        Log.d("onTap", "onTap")
                        Tap(ipAddress)

                    }
                )
            }){
        Text(
            text = "Tap or Double Tap",
            modifier = Modifier.align(Alignment.Center)
        )
    }

}


@Composable
fun TouchView(onTouch: (x: Float, y: Float) -> Unit, ipAddress: String) {
    //val doubleTapCount = remember { mutableStateOf(0) }
    var doubleTapCount by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEFD5))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onTouch(dragAmount.x, dragAmount.y)
                    Log.d("TouchView", "DeltaX: ${dragAmount.x}, DeltaY: ${dragAmount.y}")
                    sendCoordinatesToServer(dragAmount.x, dragAmount.y, ipAddress)
                }


            }
    ){
        Text(
            text = "Mouse",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


// FUNCTIONS
fun Tap(ipAddress: String){

    val ipAddress = ipAddress // Replace with your server's IP address
    val port = 1234 // Replace with your server's port number
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            //val dataToSend = "$x,$y,$otherData"
            // Send the x and y coordinates to the server
            //outToServer.writeUTF(dataToSend)

            outToServer.writeUTF("4");
            // Close the socket
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace() // Handle the exception appropriately
        }
    }

}

fun doubleTap(ipAddress: String) {

    val ipAddress = ipAddress // Replace with your server's IP address
    val port = 1234 // Replace with your server's port number
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            //val dataToSend = "$x,$y,$otherData"
            // Send the x and y coordinates to the server
            //outToServer.writeUTF(dataToSend)

            outToServer.writeUTF("5");
            // Close the socket
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace() // Handle the exception appropriately
        }
    }

}

fun sendCoordinatesToServer(x: Float, y: Float, ipAddress: String) {
    val ipAddress = ipAddress // Replace with your server's IP address
    val port = 1234 // Replace with your server's port number

    GlobalScope.launch(Dispatchers.IO) {
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            //val dataToSend = "$x,$y,$otherData"
            // Send the x and y coordinates to the server
            //outToServer.writeUTF(dataToSend)

            outToServer.writeUTF("1");
            outToServer.writeUTF("$x");
            outToServer.writeUTF("$y");
            // Close the socket
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace() // Handle the exception appropriately
        }
    }
}

fun leftClick(ipAddress: String) {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("2")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun rightClick(ipAddress: String) {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("3")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun showKeyboard(context: android.content.Context, ipAddress: String) {
    // Create an EditText to capture key events
    val editText = EditText(context)

    // Set the width and height to 0 to hide the text on the screen
    editText.layoutParams = ViewGroup.LayoutParams(0, 0)

    // Add the EditText to the layout to make it focusable
    val rootView = (context as Activity).findViewById<ViewGroup>(android.R.id.content)
    rootView.addView(editText)

    // Set up a TextWatcher to listen for text changes
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Log the changed text
            Log.d("Text Changed", s.toString()) // entire string
            Log.d("Text Changed", s?.lastOrNull().toString()) // last character
            var unicodeval = s?.lastOrNull()?.toInt()?.toString(16)?.toUpperCase() // unicode
            Log.d("Text Changed", "\\u$unicodeval")
            Log.d("Text Changed", s?.lastOrNull()?.toInt().toString())

            GlobalScope.launch(Dispatchers.IO) {
                val ipAddress = ipAddress
                val port = 1234
                try {
                    val socket = Socket(ipAddress, port)
                    val outToServer = DataOutputStream(socket.getOutputStream())
                    outToServer.writeUTF("6")
                    outToServer.writeUTF(s?.lastOrNull().toString())
                    socket.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // Not used
        }
    }

    editText.addTextChangedListener(textWatcher)

    // Request focus for the EditText to show the keyboard
    editText.requestFocus()

    // Open the keyboard
    val inputMethodManager =
        context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)

    // Capture key events
    Log.d("showKeyboard", "showKeyboard")
}

fun screenshot(ipAddress: String){

    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("7")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

fun scrollUp(ipAddress: String){
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("8")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun scrollDown(ipAddress: String){
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("9")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun shortcuts(ipAddress: String){

}


