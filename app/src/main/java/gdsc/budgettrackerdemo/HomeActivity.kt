package gdsc.budgettrackerdemo

import android.app.Activity
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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.PopupProperties
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
                    val data = remember {
                        mutableStateOf(0)
                    }
                    val sharedPreferences : SharedPreferences = context.getSharedPreferences("BUDGET",Context.MODE_PRIVATE)
                    val input_budget = sharedPreferences.getInt("BUDGET_AMT",0)

                    data.value = update_progressBar(context , input_budget)
                    Scaffold(
                        topBar = {
                            MyTopAppBar()
                        },
                        floatingActionButtonPosition = FabPosition.End,
                        content = {
                            Column(modifier = Modifier.padding(top = 0.dp), verticalArrangement = Arrangement.Center
                                ,horizontalAlignment = Alignment.CenterHorizontally) {

                                ShowBudgetBar(data.value)
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 20.dp,
                                        bottom = 10.dp
                                    )
                                    .height(1.dp)
                                    .background(Color.Gray))
                                val dbhelper = Dbhelper(context,null)
                                exp_List.clear()
                                exp_List.addAll(dbhelper.show_expense())
                                SetData(returnPercentage = {
                                    data.value = it;

                                }, returnSetData = {
                                    exp_List.clear()
                                    exp_List.addAll(it)
                                })
                                PastData(exp_List , returnSetData = {
                                    exp_List.clear()
                                    exp_List.addAll(it)
                                } , returnPregress = {
                                    data.value = it
                                })
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
fun ShowBudgetBar(value : Int){
    val hw = 180.dp
    val context :Context = LocalContext.current
    val sharedPreferences : SharedPreferences = context.getSharedPreferences("BUDGET",Context.MODE_PRIVATE)
    val input_budget = sharedPreferences.getInt("BUDGET_AMT",0)
    var progress_per :Int = value
    if (value> 100) {
        progress_per = 100;
    }

    val temp:Float = (progress_per.toFloat()/100)
    var mColor = colorResource(id = R.color.greenShade)
    if (progress_per in 46..65){
        mColor = colorResource(id = R.color.danger)
    }
    if (progress_per >= 65){
        mColor = Color.Red
    }

    Box(modifier = Modifier
        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        .height(height = hw)
        .width(width = hw)
        .clip(shape = CircleShape)
        .background(color = colorResource(id = R.color.whiteShade)), contentAlignment = Alignment.Center){
        var animationPlayed by remember {
            mutableStateOf(false);
        }
        val currentPercentage = animateFloatAsState(
            targetValue = if (animationPlayed)  temp else 0f,
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
            drawArc(color = mColor ,
                -90f ,
                360 * currentPercentage.value ,
                useCenter = false ,
                style = Stroke(30.dp.toPx() ,
                    cap = StrokeCap.Square))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center) {
            Text(
                fontSize = 39.sp ,
                textAlign = TextAlign.Center,
                text = "$progress_per%",
                color = colorName ,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                fontSize = 20.sp ,
                textAlign = TextAlign.Center,
                text = "OF\n$input_budget",
                color = Color.Black ,
                fontWeight = FontWeight.Medium
            )
        }
    }
    Text(
        fontSize = 29.sp ,
        textAlign = TextAlign.Center,
        text = ("Budget Used").toString().uppercase(),
        color = Color.Black ,
        fontWeight = FontWeight.Bold
    )
}
@Composable
fun MyTopAppBar(iconAndTextColor: Color = Color.DarkGray) {
    val activity = LocalContext.current as Activity
    val listItems = getMenuItemsList()
    val contextForToast = LocalContext.current.applicationContext
    var expanded by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        elevation = 0.dp ,
        backgroundColor = Color.White,
        modifier = Modifier.background(colorResource(id = R.color.greenShade)),
        title = {

        },
        actions = {
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Open Options"
                )
            }
            DropdownMenu(
                modifier = Modifier.width(width = 150.dp),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                offset = DpOffset(x = (22).dp, y = (-10).dp),
            ) {
                listItems.forEach { menuItemData ->
                    DropdownMenuItem(
                        onClick = {

                            when(menuItemData.index){
                                1-> {
                                    val sharedPreferences : SharedPreferences = activity.getSharedPreferences("BUDGET",Context.MODE_PRIVATE)
                                    val editor : SharedPreferences.Editor = sharedPreferences.edit()
                                    editor.clear()
                                    editor.apply()
                                    Toast.makeText(activity,"Redirecting to Home",Toast.LENGTH_LONG).show()

                                    activity.startActivity(Intent(activity , MainActivity::class.java))
                                    activity.finishAffinity()
                                }
                                2-> {

                                }
                                3-> {
                                    activity.finishAffinity()
                                }
                            }
                            expanded = false
                        },
                        enabled = true
                    ) {
                        Spacer(modifier = Modifier.width(width = 1.dp))
                        Text(
                            text = menuItemData.text,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = iconAndTextColor
                        )
                    }
                }
            }
        }
    )
}
@Composable
fun SetData(returnSetData:(MutableList<expense_details>)->Unit,returnPercentage:(Int)->Unit){
    var expenseName by remember { mutableStateOf(TextFieldValue("")) }
    var expenseAmount by remember { mutableStateOf(TextFieldValue("")) }
    var exp_list:MutableList<expense_details> = mutableListOf<expense_details>()
    val context :Context = LocalContext.current

    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)) {
            Text(text = "Enter Recent Expense", fontWeight = FontWeight.Bold , fontSize = 20.sp)
        }
        Row(Modifier.padding(10.dp) , horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(colors = TextFieldDefaults.
            textFieldColors(
                backgroundColor = Color.Transparent ,
                focusedIndicatorColor = colorResource(id = R.color.greenShade) ,
                focusedLabelColor = colorResource(id = R.color.greenShade)) , modifier = Modifier.fillMaxWidth(0.6f), value = expenseName, onValueChange ={
                expenseName = it
            }, label = @Composable{
                Text(text = "Expense Name")
            } )
            Spacer(modifier = Modifier.width(5.dp))

            OutlinedTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = TextFieldDefaults.
            textFieldColors(
                backgroundColor = Color.Transparent ,
                focusedIndicatorColor = colorResource(id = R.color.greenShade) ,
                focusedLabelColor = colorResource(id = R.color.greenShade)), value = expenseAmount, onValueChange ={
                expenseAmount = it
            },  label = @Composable{
                Text(text = "Amount" , fontSize = 17.sp)
            }   )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.greenShade)), onClick = {
                    val db = Dbhelper(context,null)
                    db.insert_expense(expenseName.text,expenseAmount.text.toInt())

                    exp_list = db.show_expense()

                    returnSetData(exp_list)

                    Toast.makeText(context, "EXPENSE ADDED",Toast.LENGTH_LONG).show()

                    val sharedPreferences : SharedPreferences = context.getSharedPreferences("BUDGET",Context.MODE_PRIVATE)

                    val input_budget = sharedPreferences.getInt("BUDGET_AMT",0)

                    val budget_percentage = update_progressBar(context,input_budget)

                    returnPercentage(budget_percentage)
                    expenseName = TextFieldValue ("")
                    expenseAmount = TextFieldValue ("");

                }) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.5.dp, bottom = 2.5.dp)
                    .height(25.dp), textAlign = TextAlign.Center, color = Color.White , fontFamily = FontFamily.Monospace , text = "ADD EXPENSE" , fontSize = 20.sp )
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PastData(exp_List:MutableList<expense_details>,returnPregress : (Int) -> Unit, returnSetData:(MutableList<expense_details>)->Unit){
    val context = LocalContext.current
    Spacer(modifier = Modifier
        .size(10.dp))
    Column(modifier = Modifier
        .fillMaxWidth()
        .absolutePadding(left = 20.dp, right = 20.dp, bottom = 0.dp)) {
        val fontsize : TextUnit = 25.sp
        Text(text = "Recents" , fontSize = fontsize , fontWeight = FontWeight(weight = 600) )
        //for space between recent and list
        Spacer(modifier = Modifier.size(10.dp))

        LazyColumn(modifier = Modifier.padding(10.dp , bottom = 20.dp), content = {
            items(count = exp_List.size){
                Card(modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            Toast
                                .makeText(context, "Press and Hold To Delete", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onLongClick = {
                            val exp_name_delete = exp_List
                                .get(it)
                                .getExpName()
                            Toast
                                .makeText(
                                    context,
                                    "Expense Removed" + exp_name_delete,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            val db = Dbhelper(context, null)
                            db.delete_expense(exp_name_delete)
                            returnSetData(db.show_expense())
                            val sharedPreferences: SharedPreferences =
                                context.getSharedPreferences("BUDGET", Context.MODE_PRIVATE)
                            val input_budget = sharedPreferences.getInt("BUDGET_AMT", 0)
                            returnPregress(update_progressBar(context, input_budget))

                        },
                    )
                    .padding(1.dp) ,
                    elevation = 2.dp,
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(8.dp)) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = exp_List[it].getExpName() , modifier = Modifier.fillMaxWidth(fraction = 0.8f))
                        Text(text = "RS. " + exp_List[it].getExpAmT(), textAlign = TextAlign.End , fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
            }

        })

    }
}

fun getMenuItemsList(): ArrayList<MenuItemData> {
    val listItems = ArrayList<MenuItemData>()
    listItems.add(MenuItemData(text = "RESET BUDGET", icon = Icons.Outlined.MailOutline  , 1))
    listItems.add(MenuItemData(text = "MEET DEV's", icon = Icons.Outlined.Add , 2))
    listItems.add(MenuItemData(text = "EXIT", icon = Icons.Outlined.Warning , 3))
    return listItems
}

fun update_progressBar(context: Context,budget: Int):Int{

    val db= Dbhelper(context, null)

    val total_exp_amt = db.total_expense_amt()

    val percentage:Int = ((total_exp_amt.toFloat()/budget)*100).toInt()

    return percentage
}





data class MenuItemData(val text: String, val icon: ImageVector , val index : Int)