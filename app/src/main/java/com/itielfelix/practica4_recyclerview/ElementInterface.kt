package com.itielfelix.practica4_recyclerview
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
var allElements:MutableList<Element> = ArrayList()
val filterStringArray = listOf( "Numero atomico","Nombre","Estado fisico")
var globalFilter = "Numero atomico"
var filter = ""
var filterObject = Filter("Numero atomico", "-", family = listOf(),states = listOf())
class ElementInterface : ComponentActivity() {
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
                    readingText(this)
                    UIElementInterfaceBuilding()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun UIElementInterfaceBuilding() {
        var filterActive by rememberSaveable { mutableStateOf(false) }
        var infoActive by rememberSaveable { mutableStateOf(false) }

        var refreshList by rememberSaveable { mutableStateOf(false) }
        var order by remember{ mutableStateOf("Numero atomico") }

        var letter by remember{ mutableStateOf(filterObject.letter) }
        var familySelected = remember{ filterObject.family.toMutableList() }
        var statesSelected = remember{ filterObject.states.toMutableList() }
        val allThings = remember { elementsArray.toMutableStateList() }
        val thisContext = LocalContext.current

        val responseLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
            if(activityResult.resultCode == 1) {
                //val a = activityResult.data!!.getParcelableExtra("filter",Filter::class.java)
                val elementFamily = activityResult.data!!.getStringArrayExtra("family")!!.toMutableList()
                val states = activityResult.data!!.getStringArrayExtra("states")!!.toMutableList()
                if(filterObject.order != order){
                    order = filterObject.order
                }
                filterObject.family = elementFamily.toList()
                familySelected.clear()
                familySelected.addAll(elementFamily)
                statesSelected.clear()
                statesSelected.addAll(states)
                letter = filterObject.letter
                allThings.clear()
                allThings.addAll(elementsArray)
                refreshList = !refreshList
            }
        }


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
//                    filterActive = true
//                    infoActive = false
                    val intent = Intent(thisContext, FilterActivity::class.java)
                    intent.putExtra("filter", filterObject)
                    responseLauncher.launch(intent)

                }) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = "")
                }
            }
            LazyColumn (){
                items(allThings) { element ->
                    if(element.name.replace(" ","")[0].toString() == filterObject.letter || letter == "-"){
                        if(familySelected.isEmpty() || familySelected.contains(element.family)){
                            if(statesSelected.isEmpty() || statesSelected.contains(element.state)){
                                ElementCard(element = element)
                            }
                        }
                    }
                    val a = order
                    order = a
                }
            }
            if (order == "Nombre") {
                elementsArray.sortBy { it.name }
                allThings.clear()
                allThings.addAll(elementsArray)
            } else
                if (order == "Numero atomico") {
                    elementsArray.sortBy { it.atomicNum }
                    allThings.clear()
                    allThings.addAll(elementsArray)
                } else
                    if (order == "Estado fisico") {
                        elementsArray.sortBy { it.atomicNum }
                        elementsArray.sortBy { it.state }
                        allThings.clear()
                        allThings.addAll(elementsArray)
                    }
            FilterDialog(
                filterActive,
                infoActive,
                order,
                { filterActive = false; infoActive = false },
                { filterActive = false; infoActive = false })
        }
        order = globalFilter
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
                        Icon(Icons.Default.Search, "Search")
                    }
                }
            }
        }
    }

    fun readingText(context: Context):Boolean {
        var i = 0
        var a = true
        elementsArray = ArrayList()
        csvReader().open(context.assets.open("data.csv")) {
            readAllAsSequence().forEach { row ->
                if (i != 0 && i < 51){
                    if(i==0) a = filterObject.letter == "-" || row[2].replace(" ","").get(0).toString() == filterObject.letter
                    if(filterObject.letter == "-" || row[2].replace(" ","").get(0).toString() == filterObject.letter) {
                        //if(row[2] == "Oxygen")
                        val element  = Element(
                            atomicNum = row[0].toInt(),
                            symbol = row[1],
                            name = row[2],
                            atomicMass = row[3],
                            oxidationStates = row[12],
                            state = row[13],
                            family = row[row.lastIndex - 1]
                        )
                        elementsArray.add(element)
                        allElements.add(element)
                    }
            }
                i++
            }
        }
        return a
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
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = CenterHorizontally,
        ) {
            Column {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = CenterVertically,

                        ) {
                        val context = LocalContext.current
                        RadioButton(
                            selected = (text == selectedOption), modifier = Modifier.padding(8.dp),
                            onClick = {
                                onOptionSelected(text)
                            }
                        )
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
