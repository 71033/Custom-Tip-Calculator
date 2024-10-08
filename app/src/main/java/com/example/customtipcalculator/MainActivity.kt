package com.example.customtipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.customtipcalculator.ui.theme.CustomTipCalculatorTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            CustomTipCalculatorScreen()
        }
    }
}

@Composable
fun CustomTipCalculatorScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CustomTipCalculatorTheme {
            CustomTipCalculator()
        }
    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(R.string.custom_tip_calculator),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun BillAmountInput(billAmount: String, focusManager: FocusManager, onBillAmountChange: (String) -> Unit){
    TextField(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down) }),
        value = billAmount,
        label = {Text(text = stringResource(R.string.enter_bill_amount))},
        onValueChange = onBillAmountChange
    )
}

@Composable
fun TipPercentageInput(tipPercentage: String, focusManager: FocusManager, onTipPercentageChange: (String) -> Unit){
    TextField(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onNext = {focusManager.clearFocus() }),
        value = tipPercentage,
        label = {Text(text = stringResource(R.string.tip))},
        onValueChange = onTipPercentageChange
    )
}

@Composable
fun RoundUpSwitch(roundUp: Boolean, onRoundUpSwitchChange: (Boolean) -> Unit){
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = stringResource(R.string.round_up_tip), textAlign = TextAlign.Start)
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpSwitchChange
        )
    }
}

@Composable
fun TipAmountDisplay(tipAmount: String){
    Text(
        text = stringResource(R.string.tip_amount) + tipAmount,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

fun calculateTip(billAmount: String, tipPercentage: String, roundUp: Boolean = true): Double {
    if(billAmount.isNotEmpty() && tipPercentage.isNotEmpty()) {
        var tipAmount = billAmount.toDouble() * tipPercentage.toDouble() / 100
        if (roundUp) {
            tipAmount = tipAmount.roundToInt().toDouble()
        }
        return tipAmount
    }
    return 0.0
}

@Composable
fun CustomTipCalculator() {
    var billAmount by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(true) }
    var tipAmount by remember { mutableStateOf("0.00") }
    val focusManager = LocalFocusManager.current

    Column {
        Title()
        BillAmountInput(billAmount, focusManager){
            if (it.isDigitsOnly()) {
                billAmount = it
            }
        }
        TipPercentageInput(tipPercentage, focusManager){
            if (it.isDigitsOnly()) {
                tipPercentage = it
            }
        }
        RoundUpSwitch(roundUp){
            roundUp = it
        }
        tipAmount = calculateTip(billAmount, tipPercentage, roundUp).toString()
        TipAmountDisplay(tipAmount)
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTipCalculatorPreview() {
    CustomTipCalculatorScreen()
}