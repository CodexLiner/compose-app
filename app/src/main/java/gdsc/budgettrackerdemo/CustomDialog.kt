package gdsc.budgettrackerdemo

import android.R
import androidx.compose.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.*

@Composable
fun CustomDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (Any) -> Unit) {
    val txtFieldError = remember { mutableStateOf("") }
    val textFieldExpenseName = remember { mutableStateOf(value) }
    val textFieldAmount = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add Expense",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 0.9.dp,
                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) gdsc.budgettrackerdemo.R.color.purple_700 else R.color.holo_red_dark)
                                ),
                                shape = RoundedCornerShape(10)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
//                        leadingIcon = {
//                            Icon(
//                                imageVector = Icons.Filled.AccountBox,
//                                contentDescription = "",
//                                tint = colorResource(android.R.color.black),
//                                modifier = Modifier
//                                    .width(20.dp)
//                                    .height(20.dp)
//                            )
//                        },
                        placeholder = { Text(text = "Enter Name of Expense") },
                        value = textFieldExpenseName.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onValueChange = {
                            textFieldExpenseName.value = it.take(10)
                        })
                    Spacer(modifier = Modifier.height(5.dp))
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 0.9.dp,
                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) gdsc.budgettrackerdemo.R.color.purple_700 else R.color.holo_red_dark)
                                ),
                                shape = RoundedCornerShape(10)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
//                        leadingIcon = {
//                            Icon(
//                                imageVector = Icons.Filled.AccountBox,
//                                contentDescription = "",
//                                tint = colorResource(android.R.color.black),
//                                modifier = Modifier
//                                    .width(20.dp)
//                                    .height(20.dp)
//                            )
//                        },
                        placeholder = { Text(text = "Enter value") },
                        value = textFieldAmount.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            textFieldAmount.value = it.take(10)
                        })
                        Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if (textFieldAmount.value.isEmpty()) {
                                    txtFieldError.value = "Field can not be empty"
                                    return@Button
                                }
                                data class values(val expese : String , val amount : String)
                                setValue(values(textFieldExpenseName.value , textFieldAmount.value))
                                setShowDialog(false)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = gdsc.budgettrackerdemo.R.color.purple_700)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Done" , fontWeight = FontWeight(900))
                        }
                    }
                }
            }
        }
    }
}