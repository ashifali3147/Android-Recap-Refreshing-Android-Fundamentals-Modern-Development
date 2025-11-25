@file:OptIn(ExperimentalMaterial3Api::class)

package com.tlw.androidrecap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.tlw.androidrecap.basic.ActivityLifecycle
import com.tlw.androidrecap.ui.theme.AndroidRecapTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLog("onCreate")
        enableEdgeToEdge()
        setContent {
            AndroidRecapTheme {
                HomeScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showLog("onStart")
    }

    override fun onResume() {
        super.onResume()
        showLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        showLog("onPause")
    }

    override fun onStop() {
        super.onStop()
        showLog("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        showLog("onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        showLog("onRestart")
    }

    fun showLog(message: String) {
        Log.i("[DEBUG]", "MainActivity Lifecycle: $message")
    }
}


@Composable
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Recap") },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Button(
                    onClick = {
                        showSnackBar("Hi", scope, snackbarHostState)
                    }
                ) {
                    Text("GREETINGS")
                }
                Button(onClick = {
                    val intent = Intent(context, ActivityLifecycle::class.java)
                    context.startActivity(intent)
                    context as Activity
//                    context.finish()
                }) {
                    Text("ACTIVITY LIFECYCLE")
                }
            }
        }
    }
}

fun showSnackBar(message: String, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    scope.launch {
        snackbarHostState.showSnackbar(message)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidRecapTheme {
        HomeScreen()
    }
}