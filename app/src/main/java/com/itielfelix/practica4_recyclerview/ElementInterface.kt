package com.itielfelix.practica4_recyclerview
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.itielfelix.practica4_recyclerview.ui.theme.Practica4_RecyclerViewTheme
val elementsArray: MutableList<Element> = ArrayList()
val filterStringArray = listOf<String>( "Nombre","Numero atomico","Estado fisico")
class ElementInterface : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica4_RecyclerViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UIElementInterfaceBuilding()
                }
            }
        }
    }

    @Composable
    fun UIElementInterfaceBuilding() {
            var filterActive by remember{ mutableStateOf(false) }
            readingText(this)
            val thisContext = this
            Column(Modifier.fillMaxSize()) {
                Row(){
                    Spacer(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth())
                    IconButton(onClick = {
                        Toast.makeText(thisContext, "Hello, this is the filter button", Toast.LENGTH_SHORT).show()
                        filterActive = true
                    } ) {
                        Icon(imageVector = Icons.Default.Filter, contentDescription = "")
                    }
                }
                LazyColumn {
                    items(elementsArray) { element ->
                        ElementCard(element = element)
                    }
                }
                if(filterActive){
                    Dialog(onDismissRequest = { /*TODO*/ }) {
                        Column(Modifier.fillMaxWidth().fillMaxHeight()){
                            SimpleRadioButtonComponent(filterArray = filterStringArray)
                        }
                        Button(onClick = { filterActive = false}){

                        }
                        Text("Dimiss")

                    }
                }
            }
    }

    @Composable
    fun ElementCard(element: Element) {
        val filterActive by remember{mutableStateOf(true)}
        val thisContext = LocalContext.current
        val elementImageName = "e"+ element.atomicNum.toString()
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    Toast
                        .makeText(thisContext, elementImageName, Toast.LENGTH_SHORT)
                        .show()
                },
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(id = thisContext.resources.getIdentifier(elementImageName,"drawable",thisContext.packageName)),
                    contentDescription = element.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                   Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically) {
                       Column(
                           verticalArrangement = Arrangement.Center,
                           horizontalAlignment = Alignment.Start
                       ) {
                           Row(){
                               Text(
                                   text = element.atomicNum.toString() + "",
                                   fontSize = 18.sp,
                                   fontWeight = FontWeight.Bold,
                                   modifier = Modifier.padding(top = 5.dp, start = 30.dp)
                               )
                               Text(
                                   text = element.symbol,
                                   color = selectColor(element.state),
                                   fontSize = 18.sp,
                                   fontWeight = FontWeight.Bold,
                                   modifier = Modifier.padding(top = 5.dp)
                               )
                           }
                           Text(
                               text = element.name,
                               fontSize = 12.sp,
                               modifier = Modifier.padding(top = 8.dp, start = 30.dp)
                           )
                       }
                       Button(onClick = { /* ... */ }) {
                           Icon(Icons.Default.Search, "Search")
                       }
                   }
                    /*Button(onClick = {
                    },
                    modifier = Modifier.padding(top= 8.dp, start =30.dp)). {
                        Text(text = "Look for it")
                    }*/
                }
            }
        }
    }

    fun readingText(context:Context) {
        var i = 0
        csvReader().open(context.assets.open("data.csv")) {
            readAllAsSequence().forEach { row ->
                if (i!=0 && i<51)
                    elementsArray.add(
                        Element(row[0].toInt(),row[1], row[2], row[3], row[12],row[13])
                    )
                i++
            }
        }
    }

    fun selectColor(color:String): androidx.compose.ui.graphics.Color {
        if (color == "liquid"){
            return Color(0xFFFF5100)
        }else if(color == "solid")
            return Color(0xFFFFFFFF)
            else
                return Color(0xFF00EBA0)
    }


    @Composable
    fun SimpleRadioButtonComponent(filterArray:List<String>) {
        val radioOptions = filterArray
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[2]) }
        Column(
            // we are using column to align our 
            // imageview to center of the screen.

            // below line is used for
            // specifying vertical arrangement.
            verticalArrangement = Arrangement.Center,

            // below line is used for
            // specifying horizontal arrangement.
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // we are displaying all our
            // radio buttons in column.
            Column {
                // below line is use to set data to
                // each radio button in columns.
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            // using modifier to add max 
                            // width to our radio button.
                            .fillMaxWidth()
                            // below method is use to add
                            // selectable to our radio button.
                            .selectable(
                                // this method is called when
                                // radio button is selected.
                                selected = (text == selectedOption),
                                // below method is called on
                                // clicking of radio button.
                                onClick = { onOptionSelected(text) }
                            )
                            // below line is use to add
                            // padding to radio button.
                            .padding(horizontal = 16.dp)
                    ) {
                        // below line is use to get context.
                        val context = LocalContext.current

                        // below line is use to
                        // generate radio button
                        RadioButton(
                            // inside this method we are
                            // adding selected with a option.
                            selected = (text == selectedOption),modifier = Modifier.padding(8.dp),
                            onClick = {
                                // inside on click method we are setting a
                                // selected option of our radio buttons.
                                onOptionSelected(text)

                                // after clicking a radio button 
                                // we are displaying a toast message.
                                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                            }
                        )
                        // below line is use to add
                        // text to our radio buttons.
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
