package com.example.calculatorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.mutableIntSetOf
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculatorapp.ui.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainPage()
        }
    }
}

@Preview
@Composable
fun MainPage() {

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopHeader(
                totalPerPerson = 125.0
            )
            Spacer(modifier = Modifier.height(24.dp))
            MainContent()
        }


    }
}


@Composable
fun TopHeader(totalPerPerson: Double) {
    val total = "%.2f".format(totalPerPerson.toFloat())

    Surface(
        color = Color(color = 0xFFE9D7F7),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(150.dp)
            .clip(shape = RoundedCornerShape(size = 12.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun MainContent() {
    BillForm { billAmt ->
        Log.d("AMT", "MainContent: $billAmt")
    }

}

@Composable
fun BillForm(
    onValChange: (String) -> Unit = {}
) {
    val billState = remember {
        mutableStateOf("")
    }

    val validState = remember(billState.value) {
        billState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = remember(sliderPositionState.value) {
        (sliderPositionState.value * 100).toInt()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val splitByState = remember {
        mutableStateOf(1)
    }

    val splitRange = IntRange(start = 1, endInclusive = 10)

    fun calclulateToTotalTip(bill: Double, tipPercent: Int): Double {
        return if (bill > 1 && bill.toString().isNotEmpty()) (bill * tipPercent) / 100 else 0.0
    }
    val tipAmountState = remember(key1 = billState.value, key2 = tipPercentage) {
        mutableStateOf(
            calclulateToTotalTip(bill = billState.value.toDoubleOrNull() ?: 0.0, tipPercent = tipPercentage)
        )
    }



    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp), shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.LightGray),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                value = billState.value,
                onValueChange = {
                    billState.value = it
                },
                label = { Text(text = "Bill") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Bill Icon"
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (validState) {
                            onValChange(billState.value.trim())
                            keyboardController?.hide()
                        }
                    }
                )
            )
            if (validState) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        RoundIconButton().RoundIconBtn(
                            modifier = Modifier,
                            imageVector = Icons.Default.KeyboardArrowDown,
                            onClick = {
                                Log.d("Split", "MainContent: Minus")
                                splitByState.value =
                                    if (splitByState.value > splitRange.start) splitByState.value - 1 else splitRange.start

                            }
                        )
                        Text(
                            text = "${splitByState.value}",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        RoundIconButton().RoundIconBtn(
                            modifier = Modifier,
                            imageVector = Icons.Default.KeyboardArrowUp,
                            onClick = {
                                Log.d("Split", "MainContent: Plus")
                                splitByState.value =
                                    if (splitByState.value < splitRange.last) splitByState.value + 1 else splitRange.last

                            }
                        )

                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$${tipAmountState.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(7.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${tipPercentage} %",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = { newVal ->
                            sliderPositionState.value = newVal
                            Log.d("Slider", "MainContent: ${sliderPositionState.value}")
                            tipAmountState.value =
                                calclulateToTotalTip(bill = billState.value.toDouble(), tipPercent = tipPercentage)
                        },
                        steps = splitByState.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        onValueChangeFinished = {
                            Log.d("Slider", "最後に呼ばれる関数: ${sliderPositionState.value}")
                        },
                    )
                }


            } else {
                Box() {

                }
            }

        }
    }

}