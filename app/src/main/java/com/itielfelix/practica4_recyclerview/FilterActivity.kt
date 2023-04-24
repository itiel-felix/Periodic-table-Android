package com.itielfelix.practica4_recyclerview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itielfelix.practica4_recyclerview.ui.theme.Practica4_RecyclerViewTheme
var letterList = listOf("-","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
var selectedLetter = ""
var selectedItems:MutableList<String> = mutableListOf( "noble gas", "earth metal", "halogen", "nonmetal","transition metal","alkali metal")
var statesList:MutableList<String> = mutableListOf("liquid", "gas", "solid")

class FilterActivity : ComponentActivity() {
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
                    UIBUilding()
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable//Use chips for familys
    fun UIBUilding() {
        LocalContext.current
        var list: List<String>
        var states: List<String>
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
            ) {
                Text("Filter", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.Center) {
                Text("Order by", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            val order = simpleRadioButtonComponent(filterArray = filterStringArray, filterObject.order)
            Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.Center) {
                Text("Select families", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            list = ChipGroup(names = selectedItems)
            Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.Center) {
                Text("Select physical states", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            states = ChipStateGroup(names = statesList)
            DropDownMenu()
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
                    onClick = { globalFilter = filter
                        val returned = Intent()
                        filterObject.order = order
                        val statesList = states.toTypedArray()
                        val familyList = list.toTypedArray()
                        filterObject.family = list
                        filterObject.states = states
                        returned.putExtra("family", familyList)
                        returned.putExtra("states",statesList)
                        setResult(1, returned)
                        finish()
                              },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Confirm")
                }
            }

        }
    }
    @Composable
    fun simpleRadioButtonComponent(filterArray: List<String>, selectedFilter: String): String {
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(selectedFilter) }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column {
                    filterArray.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            LocalContext.current
                            RadioButton(
                                selected = (text == selectedOption),
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    onOptionSelected(text)
                                }
                            )
                            Text(
                                text = text,
                                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                                color = Color.White
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
        LocalContext.current.applicationContext

        // state of the menu
        var expanded by remember {
            mutableStateOf(false)
        }

        // remember the selected item
        var selectedItem by remember {
            mutableStateOf(filterObject.letter)
        }

        // box
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.size(width = 115.dp, height = 60.dp)
        ) {
            // text field
            TextField(
                value = selectedItem,
                onValueChange = { selectedLetter = selectedItem},
                readOnly = true,
                label = { Text(text = "Letter", color = Color.White) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(textColor = Color.White)
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
                        expanded = false
                        filterObject.letter = selectedItem
                    }) {
                        Text(text = selectedOption, color= Color.White)
                    }
                }
            }
        }
            IconButton(onClick = { selectedLetter = "-"; selectedItem =listItems[0]; filterObject.letter="-" }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Default")
            }
        }
    }

    @ExperimentalMaterial3Api
    @Composable
    fun ChipGroup(
        names: MutableList<String>
    ) :MutableList<String>{
        //var selectedItems:MutableList<String> = mutableListOf( "noble gas", "earth metal", "halogen", "nonmetal","transition metal","alkali metal")
        val firstFamilies = mutableListOf(names[0],names[1])
        val lastFamilies = mutableListOf(names[3],names[4])
        val lastLastFamilies = mutableListOf(names[5],names[2])
        val familyList = remember{filterObject.family.toMutableList()}
        val leadingIcon: @Composable () -> Unit = { Icon(Icons.Default.Check, null) }
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                firstFamilies.forEach { family ->
                    var selected by remember { mutableStateOf(filterObject.family.contains(family)) }
                    FilterChip(
                        modifier = Modifier.padding(end = 8.dp),
                        selected = selected,
                        onClick = { selected = !selected
                            if(selected) {
                                familyList.add(family)
                            }
                            else{
                                if(familyList.contains(family)){
                                    familyList.remove(family)
                                }
                            }
                                  },
                        label = {Text(family)},
                        leadingIcon = if (selected) leadingIcon else null
                    )

                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                lastFamilies.forEach { family ->
                    var selected by remember { mutableStateOf(filterObject.family.contains(family)) }
                    FilterChip(
                        modifier = Modifier.padding(end = 8.dp),
                        selected = selected,
                        onClick = { selected = !selected
                            if(selected) {
                                familyList.add(family)
                            }
                            else{
                                if(familyList.contains(family)){
                                    familyList.remove(family)
                                }
                            }
                        },
                        label = {Text(family)},
                        leadingIcon = if (selected) leadingIcon else null
                    )

                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                lastLastFamilies.forEach { family ->
                    var selected by remember { mutableStateOf(filterObject.family.contains(family)) }
                    FilterChip(
                        modifier = Modifier.padding(end = 8.dp),
                        selected = selected,
                        onClick = { selected = !selected
                            if(selected) {
                                familyList.add(family)
                            }
                            else{
                                if(familyList.contains(family)){
                                    familyList.remove(family)
                                }
                            }
                        },
                        label = {Text(family)},
                        leadingIcon = if (selected) leadingIcon else null
                    )

                }
            }
        }
        return familyList
    }

    @ExperimentalMaterial3Api
    @Composable
    fun ChipStateGroup(
        names: MutableList<String>
    ) :MutableList<String>{
        val statesList = remember{filterObject.states.toMutableList()}
        val leadingIcon: @Composable () -> Unit = { Icon(Icons.Default.Check, null) }
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                names.forEach { name ->
                    var selected by remember { mutableStateOf(filterObject.states.contains(name)) }
                    FilterChip(
                        modifier = Modifier.padding(end = 8.dp),
                        selected = selected,
                        onClick = { selected = !selected
                            if(selected) {
                                statesList.add(name)
                            }
                            else{
                                if(statesList.contains(name)){
                                    statesList.remove(name)
                                }
                            }
                        },
                        label = {Text(name)},
                        leadingIcon = if (selected) leadingIcon else null
                    )

                }
            }
        }
        return statesList
    }
   
        /*if(stateList.size<3)
        return stateList
        else return listOf("Mayor a tres")*/
    
}
