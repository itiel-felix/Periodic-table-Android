package com.itielfelix.practica4_recyclerview

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itielfelix.practica4_recyclerview.ui.theme.Practica4_RecyclerViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica4_RecyclerViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UIBuilding()
                }
            }
        }
    }
}
@Preview
@Composable
fun UIBuilding() {
    val mContext = LocalContext.current
    val logo = if (isSystemInDarkTheme()) R.drawable.chemical_dark else R.drawable.chemical_light
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.padding(50.dp).height(300.dp)){
            Image(painterResource(logo), contentDescription = "light logo", Modifier.size(300.dp))
        }
        Button(onClick = {
            val intent = Intent(mContext, ElementInterface::class.java)
            mContext.startActivity(intent)
        }) {
            Text("Go to periodic table")
        }
    }
}


