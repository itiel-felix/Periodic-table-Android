package com.itielfelix.practica4_recyclerview

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itielfelix.practica4_recyclerview.ui.theme.Practica4_RecyclerViewTheme

class ElementInfo : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica4_RecyclerViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val element = intent.getParcelableExtra("element", Element::class.java)
                    UIBuilding(element = element!!)
                }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    @Composable
    fun UIBuilding(element: Element) {
        val elementImageName = "e" + element.atomicNum.toString()
        val oxidationStates = element.oxidationStates.split(',')
        val thisContext = LocalContext.current
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                Card(elevation = 10.dp,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .wrapContentSize(),){
                    Image(
                        painterResource(
                            id = thisContext.resources.getIdentifier(
                                elementImageName,
                                "drawable",
                                thisContext.packageName
                            )
                        ),
                        contentDescription = element.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                    )
                }
                AttributeFunction(attribute = "Name", value =element.name)
                AttributeFunction(attribute = "Atomic Number", value =element.atomicNum.toString())
                AttributeFunction(attribute = "Atomic Mass", value =element.atomicMass)
                OxidationStatesRow(oxidationStates = oxidationStates)
                AttributeFunction(attribute = "Physical state", value = element.state)
                AttributeFunction(attribute = "Family", value = element.family)
                /*Button(onClick = { finish()}, Modifier.padding(15.dp).size(60.dp, 60.dp).clip(CircleShape)) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription ="Back" )
                }*/

            }
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .size(80.dp)
                    .align(alignment =Alignment.BottomEnd),
                onClick = {finish()
                },
            backgroundColor = Color(99, 5, 220)) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", Modifier.size(30.dp))
            }
        }
    }
    //Element(val atomicNum:Int, val symbol: String, val name:String, val atomicMass:String, val oxidationStates:String, val state:String):Parcelable
    @Composable
    fun  AttributeFunction(attribute:String, value:String){
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp)
        ){
            Column(
                Modifier
                    .wrapContentHeight()
                    .padding(15.dp)) {
                Text(text = attribute, fontWeight = FontWeight.Bold, fontSize = 20.sp,modifier = Modifier.padding(6.dp) )
                Text(text = value, fontWeight = FontWeight.ExtraLight,modifier = Modifier.padding(2.dp))
            }
        }
    }

    @Composable
    fun DrawOxidationState(number:String){
        Box(
            Modifier
                .wrapContentSize()
                .padding(end = 20.dp, top = 10.dp)){
            if(number != "") {
                Text(
                    modifier = Modifier
                        .drawBehind {
                            drawCircle(
                                color = Color.Red,
                                radius = 35f
                            )
                        },
                    text = number,
                )
            }
            else{
                Text( modifier = Modifier
                    .drawBehind {drawCircle(
                                                    color = Color.DarkGray,
                                                    radius = 35f)
                    },
                text = "-",
            )
            }

        }
    }

    @Composable
    fun OxidationStatesRow(oxidationStates: List<String>){
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp)
        ){
            Column(
                Modifier
                    .wrapContentHeight()
                    .padding(15.dp)) {

                Row(Modifier.fillMaxWidth()) {
                    Text(text = "Oxidation states", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }
                Row(Modifier.fillMaxWidth()) {
                    oxidationStates.forEach { DrawOxidationState(number = it) }
                }
            }
        }

    }

}
