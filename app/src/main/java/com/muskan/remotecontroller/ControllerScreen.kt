package com.muskan.remotecontroller

import android.app.Activity
import androidx.compose.foundation.Image
import android.content.Context
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
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
fun SecondScreen(navController: NavHostController, text: String) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isPopupVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var screenshotFileName by remember { mutableStateOf<String?>(null) }

    val ipAddress = text.trim()

    println("ipAddress: $ipAddress")

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.navigate("main_screen") },
                modifier = Modifier
                    .height(70.dp).weight(0.5f), // Fill available space
                shape = customButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002147))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back), // Replace "your_image" with your image resource
                    contentDescription = null, // Adjust padding as needed
                )
            }
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .height(70.dp).weight(0.5f), // Fill available space
                shape = customButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002147))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shortcut_icon), // Replace "your_image" with your image resource
                    contentDescription = null, // Adjust padding as needed
                )
            }
            if (showDialog) {
                Dialog(
                    onDismissRequest = {
                        // Hide the dialog when dismissed
                        showDialog = false
                    },
                    properties = DialogProperties()
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 400.dp) // Adjust max width as needed
                            .heightIn(max = 400.dp) // Adjust max height as needed
                            .background(Color(0xFF100c08)) // Dark background color
                            .padding(16.dp) // Padding inside the dialog
                            .border(2.dp, Color.White)
                    ) {
                        // Content of the dialog
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth() // Customize the size by changing the width
                                .wrapContentHeight()
                        ) {
                            // Different buttons in the dialog
                            Button(
                                onClick = {
                                    // Add your button click action here
                                    desktop(ipAddress)
                                },
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1dacd6 ))
                            ) {
                                Text("Desktop", modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    // Add your button click action here
                                    terminal(ipAddress)
                                },
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1dacd6 ))
                            ) {
                                Text("Terminal", modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    // Add your button click action here
                                    notes(ipAddress)
                                },
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1dacd6 ))
                            ) {
                                Text("Notes", modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    // Add your button click action here
                                    calculator(ipAddress)
                                },
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1dacd6 ))
                            ) {
                                Text("Calculator", modifier = Modifier.padding(10.dp))
                            }


                            // Add more buttons as needed
                        }
                    }
                }
            }

            Button(
                onClick = {
                    GlobalScope.launch {
                        val fileName = screenshot(ipAddress)
                        if (fileName != null) {
                            screenshotFileName = fileName
                        }
                    }
                },
                modifier = Modifier
                    .height(70.dp).weight(0.5f), // Fill available space
                shape = customButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002147))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.screenshot_icon), // Replace "your_image" with your image resource
                    contentDescription = null, // Adjust padding as needed
                )
            }

            if (screenshotFileName != null) {
                AlertDialog(
                    onDismissRequest = {
                        screenshotFileName = null
                    },
                    title = {
                        Text("Screenshot Saved")
                    },
                    text = {
                        val message = screenshotFileName
                        if (message != null) {
                            Text(message)
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                screenshotFileName = null
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            Button(
                onClick = { showKeyboard(context, ipAddress) },
                modifier = Modifier
                    .height(70.dp) // Fixed height for all buttons
                    .weight(1f), // Fill available space
                shape = customButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1dacd6 ))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.keyboard_icon), // Replace "your_image" with your image resource
                    contentDescription = null, // Adjust padding as needed
                )
            }


        }
        Box(
            modifier = Modifier
                .weight(1f)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6d9bc3))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.left_click), // Replace "your_image" with your image resource
                        contentDescription = null, // Adjust padding as needed
                    )
                }

                // Scroll up, scroll down buttons arranged in a column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                ) {
                    Button(
                        onClick = { scrollUp(ipAddress) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .weight(1f),
                        shape = customButtonShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000036))
                    ) {
                        Text("Scroll Up")
                    }
                    Button(
                        onClick = { scrollDown(ipAddress) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .weight(1f),
                        shape = customButtonShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000036))
                    ) {
                        Text("Scroll Down")
                    }
                }

                // Right click button
                Button(
                    onClick = { rightClick(ipAddress) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = customButtonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6d9bc3))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.right_click), // Replace "your_image" with your image resource
                        contentDescription = null, // Adjust padding as needed
                    )
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
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFF79256),
                        Color(0xFFFBD1A2),
                        Color(0xFFF79256)
                    )
                )
            )
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
            }) {
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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0261A5),
                        Color(0xFF002A47),
                        Color(0xFF100c08),
                    )
                )
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onTouch(dragAmount.x, dragAmount.y)
                    Log.d("TouchView", "DeltaX: ${dragAmount.x}, DeltaY: ${dragAmount.y}")
                    sendCoordinatesToServer(dragAmount.x, dragAmount.y, ipAddress)
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.mouse_icon),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomEnd).size(100.dp).padding(20.dp)
        )
    }
}

fun Tap(ipAddress: String) {

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

suspend fun screenshot(ipAddress: String): String? {
    var fileName: String? = null

    withContext(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("7")
            // Receive the filename from the server
            val inputStream = socket.getInputStream()
            val dataInputStream = DataInputStream(inputStream)
            fileName = dataInputStream.readUTF()

            // Close the streams and the socket
            dataInputStream.close()
            inputStream.close()
            socket.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return fileName

}

fun scrollUp(ipAddress: String) {
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

fun scrollDown(ipAddress: String) {
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

fun desktop(ipAddress: String) {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("10")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun terminal(ipAddress: String) {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("11")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun notes(ipAddress: String) {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("12")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun calculator(ipAddress: String) {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = ipAddress
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("13")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

