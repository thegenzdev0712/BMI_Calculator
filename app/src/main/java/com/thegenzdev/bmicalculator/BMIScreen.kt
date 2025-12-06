package com.thegenzdev.bmicalculator


import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIScreen(){

    var showDialog by remember { mutableStateOf(false) }

    var bmiResult by remember { mutableStateOf("") }
    var bmiCategory by remember { mutableStateOf("") }


    var weight by remember { mutableStateOf(0) }
    var heightFt by remember { mutableStateOf(0) }
    var heightIn by remember { mutableStateOf(0) }

    var weightExpanded by remember { mutableStateOf(false) }
    var ftExpanded by remember { mutableStateOf(false) }
    var inchExpanded by remember { mutableStateOf(false) }


    Scaffold(
        topBar = { TopAppBar(title = {Text("BMI Calculator", fontFamily = FontFamily.Serif)}, colors = TopAppBarDefaults.topAppBarColors(containerColor =  Color(0xFFA5D6A7))) },
        contentWindowInsets = WindowInsets.statusBars
    ) { padding ->

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {



            Text("“Body Mass Index (BMI) is a simple calculation that uses your height and weight to estimate whether you’re in a healthy weight range. It’s commonly used as a quick screening tool to understand general health trends, but it doesn’t measure body fat directly or account for muscle mass, age, or individual differences.”\n\n", fontSize = 15.sp, minLines = 8, modifier = Modifier.padding(20.dp))

            ExposedDropdownMenuBox(
                expanded = weightExpanded,
                onExpandedChange = { weightExpanded = !weightExpanded}
            ) {
                OutlinedTextField(
                    value = if (weight == 0) " " else "$weight kg",
                    shape = RoundedCornerShape(15.dp),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable,true)
                        .width(285.dp),
                    label = { Text("Weight (kg)")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(weightExpanded) }
                )

                ExposedDropdownMenu(
                    expanded = weightExpanded,
                    onDismissRequest = { weightExpanded = false}
                ) {
                    (45..200).forEach { w ->
                        DropdownMenuItem(
                            text = { Text("$w kg")},
                            onClick = {
                                weight = w
                                weightExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                ExposedDropdownMenuBox(
                    expanded = ftExpanded,
                    onExpandedChange = { ftExpanded = !ftExpanded }
                ) {
                    OutlinedTextField(
                        value = if (heightFt == 0) " " else "$heightFt ft",
                        shape = RoundedCornerShape(15.dp),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .width(140.dp),
                        label = { Text("Height (ft)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(ftExpanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = ftExpanded,
                        onDismissRequest = { ftExpanded = false }
                    ) {
                        (4..7).forEach { ft ->
                            DropdownMenuItem(
                                text = { Text("$ft feet") },
                                onClick = {
                                    heightFt = ft
                                    ftExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = inchExpanded,
                    onExpandedChange = { inchExpanded = !inchExpanded }
                ) {
                    OutlinedTextField(
                        value = "$heightIn in",
                        shape = RoundedCornerShape(15.dp),
                        placeholder = {Text("Select Inches")},
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .width(140.dp),
                        label = { Text("Height (in)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(inchExpanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = inchExpanded,
                        onDismissRequest = { inchExpanded = false }
                    ) {
                        (0..11).forEach { inch ->
                            DropdownMenuItem(
                                text = { Text("$inch inches") },
                                onClick = {
                                    heightIn = inch
                                    inchExpanded = false
                                }
                            )
                        }
                    }
                }

            }

            Spacer(Modifier.height(5.dp))

            Button(onClick = {
                if (heightFt > 0 && weight > 0) {
                    val totalInches = (heightFt * 12) + heightIn
                    val heightMeters = totalInches * 0.0254
                    val bmi = weight / (heightMeters * heightMeters)

                    bmiResult = String.format("%.1f", bmi)

                    bmiCategory = when {
                        bmi < 18.5 -> "Underweight \uD83E\uDD7A\uD83C\uDF43"
                        bmi < 24.9 -> "Healthy Weight \uD83C\uDF4F✨"
                        bmi < 29.9 -> "Overweight \uD83C\uDF54\uD83D\uDE05"
                        else -> "Obese \uD83D\uDEB6\u200D♂\uFE0F\uD83D\uDCC9"
                    }

                    showDialog = true

                    weight = 0
                    heightFt = 0
                    heightIn = 0


                    weightExpanded = false
                    ftExpanded = false
                    inchExpanded = false
                }
            }, modifier = Modifier.width(150.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )) {
                Text("Calculate BMI")
            }

            Spacer(Modifier.height(25.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(140.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor =  Color(0xFFA5D6A7),
                    contentColor = Color.Black
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Text(
                        "“The secret of getting ahead is getting started.” — Mark Twain", fontSize = 20.sp, modifier = Modifier.padding(10.dp),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Text(
                        "OK",
                        modifier = Modifier.padding(8.dp)
                            .clickable { showDialog = false }
                    )
                },
                title = {
                    Text("Your BMI Result", fontFamily = FontFamily.Serif)
                },
                text = {
                    Column {
                        Text("BMI: $bmiResult",fontWeight = FontWeight.Bold)
                        Text("Category: $bmiCategory", fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color(0xFFE8F5E9), // soft green
                tonalElevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            )
        }


    }
}














