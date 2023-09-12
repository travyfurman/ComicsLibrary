@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.comicslibrary.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.comicslibrary.CharacterImage
import com.example.comicslibrary.R
import com.example.comicslibrary.model.Note
import com.example.comicslibrary.model.db.DBNote
import com.example.comicslibrary.ui.theme.GreyBackground
import com.example.comicslibrary.ui.theme.GreyTransparentBackground

import com.example.comicslibrary.viewmodel.CollectionDbViewModel

@Composable
fun CollectionScreen(cvm: CollectionDbViewModel, navController: NavHostController) {

    val charactersInCollection = cvm.collection.collectAsState()
    val expandedElement = remember { mutableStateOf(-1) }
    val notes = cvm.notes.collectAsState()

    if (charactersInCollection.value.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "When you add characters from the library, they will appear here!",
                modifier = Modifier.padding(top = 16.dp)
            )
            Image(painterResource(id = R.drawable.marvel_logo), contentDescription = null,
                Modifier.weight(1f))
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(charactersInCollection.value) { character ->
                Column {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(4.dp)
                        .clickable {
                            if (expandedElement.value == character.id) {
                                expandedElement.value = -1
                            } else {
                                expandedElement.value = character.id
                            }
                        }) {
                        CharacterImage(
                            url = character.thumbnail,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxHeight(),
                            contentScale = ContentScale.FillHeight
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = character.name ?: "No name",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                maxLines = 2
                            )
                            Text(text = character.comics ?: "", fontStyle = FontStyle.Italic)
                        }

                        Column(
                            modifier = Modifier
                                .wrapContentWidth()
                                .fillMaxHeight()
                                .padding(4.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    cvm.deleteCharacter(character)
                                }
                            )

                            if (character.id == expandedElement.value)
                                Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = null)
                            else
                                Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null)
                        }
                    }
                }

                if (character.id == expandedElement.value) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GreyTransparentBackground)
                    ) {
                        val filteredNotes = notes.value.filter { note ->
                            note.characterId == character.id
                        }
                        NotesList(filteredNotes, cvm)
                        CreateNoteForm(character.id, cvm)
                    }
                }

                Divider(
                    color = Color.LightGray,
                    modifier = Modifier.padding(
                        top = 4.dp, bottom = 4.dp, start = 20.dp, end = 20.dp
                    )
                )
            }
        }
    }

}

@Composable
fun NotesList(notes: List<DBNote>, cvm: CollectionDbViewModel) {
    for (note in notes) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(GreyBackground)
                .padding(4.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, fontWeight = FontWeight.Bold)
                Text(text = note.text)
            }
            Icon(
                Icons.Outlined.Delete,
                contentDescription = null,
                modifier = Modifier.clickable {
                    cvm.deleteNote(note)
                })
        }
    }
}

@Composable
fun CreateNoteForm(characterId: Int, cvm: CollectionDbViewModel) {
    val addNoteToElement = remember { mutableStateOf(-1) }
    val newNoteTitle = remember { mutableStateOf("") }
    val newNoteText = remember { mutableStateOf("") }

    if (addNoteToElement.value == characterId) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .background(GreyBackground)
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = "Add Note", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = newNoteTitle.value,
                onValueChange = { newNoteTitle.value = it },
                label = { Text(text = "Note Title") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newNoteText.value,
                    onValueChange = { newNoteText.value = it },
                    label = { Text(text = "Note Content") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Button(onClick = {
                    val note = Note(characterId, newNoteTitle.value, newNoteText.value)
                    cvm.addNote(note)
                    newNoteTitle.value = ""
                    newNoteText.value = ""
                    addNoteToElement.value = -1
                }) {
                    Text(text = "Save")
                }
            }
        }
    }

    Button(onClick = { addNoteToElement.value = characterId }) {
        Text(text = "Add note")
    }

}