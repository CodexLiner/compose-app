package gdsc.budgettrackerdemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gdsc.budgettrackerdemo.ui.theme.BudgetTrackerDemoTheme

class HomeActivity : ComponentActivity() {

    var exp_List:MutableList<expense_details> = mutableStateListOf<expense_details>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val context = LocalContext.current
                    val isOpened = remember {
                        mutableStateOf(false)
                    }
                    Scaffold(
                        floatingActionButtonPosition = FabPosition.End,
                        content = {
                            Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.Center
                                , horizontalAlignment = Alignment.CenterHorizontally) {
                                ShowBudgetBar()
                                var dbhelper = Dbhelper(context,null)
                                exp_List.addAll(dbhelper.show_expense())
                                setData()
                                {
                                    exp_List.clear()
                                    exp_List.addAll(it)
                                }
//                                      exp_List.addAll(setData())
                                PastData(exp_List)
                            }
                        })
                    if (isOpened.value){
                        CustomDialog(value = "", setShowDialog = {
                            isOpened.value = it
                        }){
//                            Toast.makeText(context , it , Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowBudgetBar(){
    Box(modifier = Modifier
        .padding(10.dp)
        .height(height = 200.dp)
        .width(width = 200.dp)
        .clip(shape = CircleShape)
        .background(color = colorResource(id = R.color.whiteShade)), contentAlignment = Alignment.Center){
        var animationPlayed by remember {
            mutableStateOf(false);
        }
        val currentPercentage = animateFloatAsState(
            targetValue = if (animationPlayed)  0.2f else 0f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 0
            )
        )
        LaunchedEffect(key1 = true, block ={
            animationPlayed = true
        } )
        val colorName = colorResource(id = R.color.greenShade)
        Canvas(modifier = Modifier.size(size = 300.dp) ){
            drawArc(color = colorName ,
                -90f ,
                360 * currentPercentage.value ,
                useCenter = false ,
                style = Stroke(30.dp.toPx() ,
                    cap = StrokeCap.Square))
        }
        val fontsize : TextUnit = 39.sp;
        Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center) {
            Text(
                fontSize = fontsize ,
                textAlign = TextAlign.Center,
                text = "10%",
                color = colorName ,
                fontWeight = FontWeight.Bold
            )
            val fontsize : TextUnit = 20.sp;
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                fontSize = fontsize ,
                textAlign = TextAlign.Center,
                text = "Budget Used",
                color = Color.Black ,
                fontWeight = FontWeight.Bold
            )
        }
    }

    var context : Context = LocalContext.current
    var sharedPreferences :  SharedPreferences = context.getSharedPreferences("BUDGET",Context.MODE_PRIVATE)
    var budget_amt : Int = sharedPreferences.getInt("BUDGET_AMT",0);

    Toast.makeText(context,""+budget_amt,Toast.LENGTH_LONG).show()

}


@Composable
fun setData(returnSetData:(MutableList<expense_details>)->Unit){

    var exp_list:MutableList<expense_details> = mutableListOf<expense_details>()
    var expenseName by remember { mutableStateOf(TextFieldValue("")) }
    var expenseAmount by remember { mutableStateOf(TextFieldValue("")) }


    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)) {
            Text(text = "Enter Recent Expense", fontWeight = FontWeight.Bold)
        }
        Row(Modifier.padding(10.dp) , horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(modifier = Modifier.fillMaxWidth(0.6f), value = expenseName, onValueChange ={
                expenseName = it
            }, label = @Composable{
                Text(text = "Expense Name")
            } )
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedTextField(value = expenseAmount, onValueChange ={
                expenseAmount = it
            },  label = @Composable{
                Text(text = "Amount" , fontSize = 17.sp)
            }   )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp), verticalAlignment = Alignment.CenterVertically) {

            val context = LocalContext.current
            Button(colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.greenShade)), onClick = {

                Toast.makeText(context,""+expenseName.text+expenseAmount.text,Toast.LENGTH_LONG).show()

//                var temp_exp = expense_details(expenseName.text,expenseAmount.text.toInt())
                var db = Dbhelper(context,null)
                db.insert_expense(expenseName.text,expenseAmount.text.toInt())

                exp_list = db.show_expense()

                returnSetData(exp_list)

            }) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp), textAlign = TextAlign.Center, text = "ADD EXPENSE" , fontSize = 20.sp , color = Color.White)
            }
        }
    }



}


@Composable
fun PastData(exp_List:MutableList<expense_details>){


//    Log.d("DATABASE_UPDATE",exp_List.get(0).getExpName())
    Spacer(modifier = Modifier
        .size(10.dp))
    Column(modifier = Modifier
        .fillMaxWidth()
        .absolutePadding(left = 15.dp, right = 15.dp)) {
        val fontsize : TextUnit = 25.sp
        Text(text = "Recents" , fontSize = fontsize , fontWeight = FontWeight(weight = 600) )
        //for space between recent and list
        Spacer(modifier = Modifier.size(10.dp))


        LazyColumn(modifier = Modifier.padding(10.dp , bottom = 20.dp), content = {
            items(count = exp_List.size){
                Card(modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(1.dp) ,
                    elevation = 4.dp,
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(10.dp)) {
                    Row(modifier = Modifier.padding(10.dp) , verticalAlignment = Alignment.CenterVertically) {
                        Text(text = exp_List[it].getExpName() , modifier = Modifier.fillMaxWidth(fraction = 0.8f))
                        Text(text = "RS. " + exp_List[it].exp_Amt, textAlign = TextAlign.End , fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
            }

        })
        Spacer(modifier = Modifier.size(100.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BudgetTrackerDemoTheme {
        // Greeting("Android")
    }
}