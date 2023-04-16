package com.itielfelix.practica4_recyclerview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.itielfelix.practica4_recyclerview.ui.theme.Practica4_RecyclerViewTheme
var selectedFilter = "Numero atomico"
var letterList = listOf("-","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
var selectedLetter = ""
var selectedItems:MutableList<String> = mutableListOf("Metals", "Noble gases", "Non metal", "Halogen")
class FilterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica4_RecyclerViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UIBUilding()
                }
            }
        }
    }


    @Composable//Use chips for familys
    fun UIBUilding() {
        // Draw a rectangle shape with rounded corners inside the dialog
        val thisContext = LocalContext.current
        val disabledItem = 1
        Column(
            Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.DarkGray.copy(alpha = 0f))
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp), horizontalArrangement = Arrangement.Center
            ) { Text("Filter", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            filter = simpleRadioButtonComponent(filterArray = filterStringArray, selectedFilter)
            DropDownMenu()
            ChipGroup(names = selectedItems)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { finish() }, shape = RoundedCornerShape(20.dp)) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Button(
                    onClick = { globalFilter = filter; finish() },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Confirm")
                }
            }

        }
    }
    @Composable
    fun simpleRadioButtonComponent(filterArray: List<String>, selectedFilter: String): String {
            val radioOptions = filterArray
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(selectedFilter) }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            val context = LocalContext.current
                            RadioButton(
                                selected = (text == selectedOption),
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    onOptionSelected(text)
                                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DropDownMenu() {
        val listItems = letterList
        val contextForToast = LocalContext.current.applicationContext

        // state of the menu
        var expanded by remember {
            mutableStateOf(false)
        }

        // remember the selected item
        var selectedItem by remember {
            mutableStateOf(listItems[0])
        }

        // box
        Row(){
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.size(width = 105.dp, height = 60.dp)
        ) {
            // text field
            TextField(
                value = selectedItem,
                onValueChange = { selectedLetter = selectedItem},
                readOnly = true,
                label = { Text(text = "Letter") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            // menu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // this is a column scope
                // all the items are added vertically
                listItems.forEach { selectedOption ->
                    // menu item
                    DropdownMenuItem(onClick = {
                        selectedItem = selectedOption
                        selectedLetter = selectedOption
                        Toast.makeText(contextForToast, selectedLetter, Toast.LENGTH_SHORT).show()
                        expanded = false
                    }) {
                        Text(text = selectedOption)
                    }
                }
            }
        }
            IconButton(onClick = { selectedLetter = "-"; selectedItem =listItems[0] }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Default")
            }
        }
    }

    @Composable
    fun Chip(
        name: String = "Chip",
        isSelected: Boolean = false,
        onSelectionChanged: (String) -> Unit = {},
    ) {
        Surface(
            modifier = Modifier.padding(4.dp),
            elevation = 8.dp,
            shape = MaterialTheme.shapes.medium,
            color = if (isSelected) Color.LightGray else MaterialTheme.colors.primary
        ) {
            Row(modifier = Modifier
                .toggleable(
                    value = isSelected,
                    onValueChange = {
                        onSelectionChanged(name)
                    }
                )
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.body2,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

    @Composable
    fun ChipGroup(
        names: MutableList<String>,
        selectedCar: String? = null,
        onSelectedChanged: (String) -> Unit = {},
    ) {
        val newNames = names
        Column(modifier = Modifier.padding(8.dp)) {
            LazyColumn {
                items(names) {
                    Chip(
                        name = it,
                        isSelected = selectedCar == it,
                        onSelectionChanged = {
                            onSelectedChanged(it)
                        },
                    )
                }
            }
        }
    }
}
