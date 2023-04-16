package com.itielfelix.practica4_recyclerview
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.itielfelix.practica4_recyclerview.ui.theme.Practica4_RecyclerViewTheme

var elementsArray: MutableList<Element> = ArrayList()
val filterStringArray = listOf<String>( "Nombre","Numero atomico","Estado fisico")
var globalFilter = "Numero atomico"
var filter = ""
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
                    readingText(this)
                    UIElementInterfaceBuilding()
                }
            }
        }
    }

    @Composable
    fun UIElementInterfaceBuilding() {
        var filterActive by rememberSaveable { mutableStateOf(false) }
        var infoActive by rememberSaveable { mutableStateOf(false) }
        var actualFilter by remember{ mutableStateOf("Numero atomico") }
        val allThings = remember { elementsArray.toMutableStateList() }

        Column(Modifier.fillMaxSize()) {
            Row(verticalAlignment = CenterVertically) {
                IconButton(onClick = {
                    infoActive = true
                    filterActive = false
                }) {
                    Icon(imageVector = Icons.Default.QuestionMark, contentDescription = "")
                }
                Text(text = "Info")
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                Text(text = "Filter")
                IconButton(onClick = {
                    filterActive = true
                    infoActive = false

                }) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = "")
                }
            }
            LazyColumn (){
                items(allThings) { element ->
                    ElementCard(element = element)
                }
            }
            if (actualFilter == "Nombre") {
                elementsArray.sortBy { it.name }
                allThings.clear()
                allThings.addAll(elementsArray)
                //Toast.makeText(LocalContext.current, "Works with Nombre",Toast.LENGTH_SHORT).show()
            } else
                if (actualFilter == "Numero atomico") {
                    elementsArray.sortBy { it.atomicNum }
                    allThings.clear()
                    allThings.addAll(elementsArray)
                    //Toast.makeText(LocalContext.current, "Works with Numero Atomico",Toast.LENGTH_SHORT).show()
                } else
                    if (actualFilter == "Estado fisico") {
                        elementsArray.sortBy { it.atomicNum }
                        elementsArray.sortBy { it.state }
                        allThings.clear()
                        allThings.addAll(elementsArray)
                    }
            FilterDialog(
                filterActive,
                infoActive,
                actualFilter,
                { filterActive = false; infoActive = false },
                { filterActive = false; infoActive = false })
        }
        actualFilter = globalFilter
    }


    @Composable
    fun FilterDialog(
        show: Boolean, info: Boolean, selectedFilter:String,
        onDismiss: () -> Unit,
        onConfirm: () -> Unit
    ) {
        if (show) {
            Dialog(onDismissRequest = { onDismiss() }) {
                // Draw a rectangle shape with rounded corners inside the dialog
                Column(
                    Modifier
                        .size(400.dp, 330.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.DarkGray)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp), horizontalArrangement = Arrangement.Center
                    ) { Text("Filter", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                    filter = simpleRadioButtonComponent(filterArray = filterStringArray, selectedFilter)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, end = 15.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { onDismiss() }, shape = RoundedCornerShape(20.dp)) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Button(onClick = { onDismiss(); globalFilter = filter }, shape = RoundedCornerShape(20.dp)) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }

        val size = 30
        if (info) {
            Dialog(onDismissRequest = { onDismiss() }) {
                // Draw a rectangle shape with rounded corners inside the dialog
                Column(
                    Modifier
                        .size(300.dp, 250.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.DarkGray)
                        .then(Modifier.padding(30.dp)),
                    horizontalAlignment = CenterHorizontally
                )
                {
                    Text(
                        "Each element is highlighted with a color depending on its physical state.",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(Modifier.padding(10.dp)) {
                            Circle(color = Color(0xFFFF5100), size)
                            Text(text = "Liquid")
                        }
                        Row(Modifier.padding(10.dp)) {
                            Circle(color = Color(0xFFFFFFFF), size)
                            Text(text = "Solid")
                        }
                    }
                    Row(Modifier.padding(10.dp)) {
                        Circle(color = Color(0xFF00EBA0), size)
                        Text(text = "Gas")
                    }
                }
            }
        }
    }

    @Composable
    fun ElementCard(element: Element) {
        val thisContext = LocalContext.current
        val elementImageName = "e" + element.atomicNum.toString()
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    val intent = Intent(thisContext, ElementInfo::class.java)
                    intent.putExtra("element", element)
                    thisContext.startActivity(intent)
                },
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
                    .padding(8.dp),
                verticalAlignment = CenterVertically
            ) {
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
                        .size(90.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row() {
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
                }
            }
        }
    }

    fun readingText(context: Context) {
        var i = 0
        elementsArray = ArrayList()
        csvReader().open(context.assets.open("data.csv")) {
            readAllAsSequence().forEach { row ->
                if (i != 0 && i < 51)
                    elementsArray.add(
                        Element(row[0].toInt(), row[1], row[2], row[3], row[12], row[13])
                    )
                i++
            }
        }
    }

    fun selectColor(color: String): Color {
        if (color == "liquid") {
            return Color(0xFFFF5100)
        } else if (color == "solid")
            return Color(0xFFFFFFFF)
        else
            return Color(0xFF00EBA0)
    }


    @Composable
    fun simpleRadioButtonComponent(filterArray: List<String>, selectedFilter:String): String {
        val radioOptions = filterArray
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(selectedFilter) }
        Column(
            // we are using column to align our 
            // imageview to center of the screen.

            // below line is used for
            // specifying vertical arrangement.
            verticalArrangement = Arrangement.SpaceEvenly,

            // below line is used for
            // specifying horizontal arrangement.
            horizontalAlignment = CenterHorizontally,
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
//                            // selectable to our radio button.
//                            .selectable(
//                                // this method is called when
//                                // radio button is selected.
//                                selected = (text == selectedOption),
//                                // below method is called on
//                                // clicking of radio button.
//                                onClick = { onOptionSelected(text) }
//                            )
                            // below line is use to add
                            // padding to radio button.
                            .padding(horizontal = 16.dp),
                        verticalAlignment = CenterVertically,

                        ) {
                        // below line is use to get context.
                        val context = LocalContext.current

                        // below line is use to
                        // generate radio button
                        RadioButton(
                            // inside this method we are
                            // adding selected with a option.
                            selected = (text == selectedOption), modifier = Modifier.padding(8.dp),
                            onClick = {
                                // inside on click method we are setting a
                                // selected option of our radio buttons.
                                onOptionSelected(text)
                                // after clicking a radio button 
                                // we are displaying a toast message.
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                            }
                        )
                        // below line is use to add
                        // text to our radio buttons.
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                        )
                    }
                }
            }
        }
        return selectedOption
    }

    @Composable
    fun Circle(color: Color, size: Int) {
        Canvas(modifier = Modifier.size(size.dp), onDraw = {
            drawCircle(color = color)
        })
    }
}
