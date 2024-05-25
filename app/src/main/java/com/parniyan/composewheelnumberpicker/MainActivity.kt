package com.parniyan.composewheelnumberpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parniyan.composewheelnumberpicker.ui.theme.ComposeWheelNumberPickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeWheelNumberPickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val items = arrayListOf(1..40).map { it.toString() }
                    BaseNumberPicker(
                        modifier = Modifier
                            .padding(innerPadding),
                        items = items,
                        onValueChanged = {})
                }
            }
        }
    }
}
