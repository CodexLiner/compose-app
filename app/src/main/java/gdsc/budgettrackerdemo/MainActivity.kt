package gdsc.budgettrackerdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gdsc.budgettrackerdemo.ui.theme.BudgetTrackerDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

                        val context : Context = LocalContext.current
                        val activity = (LocalContext.current as? Activity)
                        val sharedPreferences : SharedPreferences = context.getSharedPreferences("BUDGET",Context.MODE_PRIVATE)
                        if(sharedPreferences.getInt("BUDGET_AMT",0) == 0)
                        {
                            Toast.makeText(LocalContext.current,"okay",Toast.LENGTH_LONG).show()
                            WelcomeWidget()
                            SetBudget();
                        }
                        else
                        {
                            val intent : Intent = Intent(context,HomeActivity::class.java)
                            context.startActivity(intent)
                            activity?.finish()
                        }


                    }
                }
            }
        }
    }
}
@Composable
fun WelcomeWidget(){
    val painter = painterResource(id = R.drawable.logo);
    Column(modifier = Modifier.padding(30.dp) , verticalArrangement = Arrangement.Top , horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "WELCOME\nSet Budget to Continue" , modifier = Modifier.padding(10.dp) ,
        fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace , fontSize = 25.sp , textAlign = TextAlign.Center
        )
        Image(painter = painter, contentDescription = "logo")
    }
}
@Composable
fun SetBudget(modifier: Modifier = Modifier){
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Column(modifier = modifier.padding(29.dp)) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = text,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            maxLines = 10,
            label =  @Composable {
                Text(
                    text = "Set Budget",
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            },
            onValueChange = {
                text = it
            }
        )
        Spacer(modifier = modifier.padding(12.dp))

        val context = LocalContext.current;

        //Creating shared preferences

        var sharedPreferences : SharedPreferences = context.getSharedPreferences("BUDGET", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()

        Button(onClick = {
            if (!text.equals(null) && text.toString().isNotEmpty()){

                editor.putInt("BUDGET_AMT",text.text.toInt())
                editor.apply()
                context.startActivity(Intent(context , HomeActivity::class.java))
            }
        }) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)  ,  horizontalArrangement = Arrangement.Center) {
                Text(text = "Done" , textAlign = TextAlign.Center , modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
