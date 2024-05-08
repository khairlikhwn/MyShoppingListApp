package com.practice.myshoppinglist

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ShoppingItems(var id: Int,
                         var name: String,
                         var quantity: Int,
                         var isEditing: Boolean = false){

}

@Composable
fun ShoppingListApp(){

    var sItems by remember{mutableStateOf(listOf<ShoppingItems>())}
    var showdialog by remember{mutableStateOf(false)}
    var itemName by remember {mutableStateOf("")}
    var itemQuantity by remember {mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    )
    {
        Button(
            onClick = {showdialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally))
        {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){
                    items ->
                if(items.isEditing){
                    ShoppingItemEditor(item = items, onEditComplete = {
                            editedname, editedquantity ->
                        sItems = sItems.map {it.copy(isEditing = false)}
                        val editedItem = sItems.find{it.id == items.id}
                        editedItem?.let {
                            it.name = editedname
                            it.quantity = editedquantity
                        }
                    })
                }
                else{
                    ShoppingListItems(item = items, onEditClick = {
                        sItems = sItems.map {it.copy(isEditing = it.id == items.id)}
                    }, onDeleteClick = {
                        sItems = sItems - items
                    })
                }

            }
        }
    }
    if (showdialog){
        AlertDialog(onDismissRequest = {showdialog = false},
            confirmButton = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Button(onClick = {

                        if(itemName.isNotBlank()){
                            val newitems = ShoppingItems(
                                id = sItems.size+1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItems = sItems + newitems
                            showdialog = false
                            itemName = ""

                        }

                    }) {
                        Text(text = "Add")
                    }

                    Button(onClick = {showdialog = false}) {
                        Text(text = "Cancel")
                    }

                }

            },
            title = { Text(text = "Add Shopping Items")},
            text = {
                Column {
                    OutlinedTextField(value = itemName ,
                        onValueChange ={itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)  )

                    OutlinedTextField(value = itemQuantity ,
                        onValueChange ={itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)  )
                }
            }
        )
    }
}

@Composable

fun ShoppingItemEditor(item: ShoppingItems, onEditComplete : (String, Int) -> Unit){

    var editedname by remember {mutableStateOf(item.name)}
    var editedquantity by remember {mutableStateOf(item.quantity.toString())}
    var isEditing by remember {mutableStateOf(item.isEditing)}

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Column {
            BasicTextField(
                value = editedname,
                onValueChange = {editedname = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)

            )

            BasicTextField(
                value = editedquantity,
                onValueChange = {editedquantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)

            )

        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedname,editedquantity.toIntOrNull() ?: 1)
        })
        {
            Text(text = "Save")
        }

    }

}




@Composable
fun ShoppingListItems(
    item : ShoppingItems,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
){
    Row(horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Cyan),
                shape = RoundedCornerShape(20),

                )
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty : ${item.quantity}",modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)) {

            IconButton(onClick = {onEditClick ()}) {
                Icon(imageVector = Icons.Default.Edit , contentDescription = null)
            }

            IconButton(onClick = {onDeleteClick ()}) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}