package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.TourItemEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDialog(
    item: TourItemEntity,
    onDismiss: () -> Unit,
    onConfirm: (date: String, guests: Int, name: String, phone: String) -> Unit
) {
    var date by remember { mutableStateOf("2026-09-15") }
    var guestsStr by remember { mutableStateOf("2") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val guests = guestsStr.toIntOrNull() ?: 1

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(text = "Réservation Nosy Vanona Tours", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date souhaitée (AAAA-MM-JJ)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = guestsStr,
                    onValueChange = { guestsStr = it },
                    label = { Text("Nombre de personnes") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom complet") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Téléphone / WhatsApp") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(date, guests, name, phone)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Confirmer la Réservation")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
