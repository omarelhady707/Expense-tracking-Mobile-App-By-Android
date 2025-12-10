package com.example.budgetplanner.UIpages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetplanner.R

@Composable
fun EditAccountScreen(
    phone: String = "+20 123 456 7890",
    address: String = "Cairo, Egypt",
    income: String = "$10,840.00",
    password: String = "********",
    onSave: (String, String, String, String) -> Unit
) {
    var phoneState = remember { mutableStateOf(phone) }
    var addressState = remember { mutableStateOf(address) }
    var incomeState = remember { mutableStateOf(income) }
    var passwordState = remember { mutableStateOf(password) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text("Edit Account", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = phoneState.value,
            onValueChange = { phoneState.value = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = addressState.value,
            onValueChange = { addressState.value = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = incomeState.value,
            onValueChange = { incomeState.value = it },
            label = { Text("Income") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                onSave(
                    phoneState.value,
                    addressState.value,
                    incomeState.value,
                    passwordState.value
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
