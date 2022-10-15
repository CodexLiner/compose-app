package gdsc.budgettrackerdemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import gdsc.budgettrackerdemo.ui.theme.BudgetTrackerDemoTheme


class Devs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.padding(30.dp), verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
                     DevsIntro()
                    }
                }
            }
        }
    }
}

@Composable
fun DevsIntro(){
    val context = LocalContext.current
    Card(modifier = Modifier
        .fillMaxWidth()
        , elevation = 10.dp) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                Image(modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .padding(start = 0.dp), painter = painterResource(id = R.drawable.dp1), contentDescription = "")

            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Gopal Meena" , fontSize = 20.sp , fontFamily = FontFamily.Monospace)
                Row(horizontalArrangement = Arrangement.Center) {
                    Image(modifier = Modifier
                        .height(50.dp)
                        .clickable { onClick("https://github.com/CodexLiner", context) }, painter = painterResource(id = R.drawable.git90), contentDescription ="" )
                    Image(modifier = Modifier
                        .height(50.dp)
                        .clickable { onClick("https://www.linkedin.com/in/meenagopal24", context) } ,painter = painterResource(id = R.drawable.link90), contentDescription ="" )

                }
                Spacer(modifier = Modifier
                    .height(0.5.dp)
                    .fillMaxWidth()
                    .background(color = Color.Gray))
                Spacer(modifier = Modifier.height(10.dp))
                Image(modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .padding(end = (0).dp), painter = painterResource(id = R.drawable.dp2), contentDescription = "")
                Text(text = "Anuroop Farkya" , fontSize = 20.sp , fontFamily = FontFamily.Monospace)
                Row(horizontalArrangement = Arrangement.Center) {
                    Image(modifier = Modifier
                        .height(50.dp)
                        .clickable { onClick("https://github.com/AnuroopFarkya11", context) }, painter = painterResource(id = R.drawable.git90), contentDescription ="" )
                    Image(modifier = Modifier
                        .height(50.dp)
                        .clickable {
                            onClick(
                                "https://www.linkedin.com/in/anuroop-farkya-4a9451213",
                                context
                            )
                        } ,painter = painterResource(id = R.drawable.link90), contentDescription ="" )

                }
            }
        }
    }
}

fun onClick(link : String , context :  Context) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    context.startActivity(browserIntent)
}
