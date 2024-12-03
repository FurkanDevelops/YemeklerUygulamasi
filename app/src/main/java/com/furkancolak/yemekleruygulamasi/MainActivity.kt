package com.furkancolak.yemekleruygulamasi

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.furkancolak.yemekleruygulamasi.ui.theme.YemeklerUygulamasiTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YemeklerUygulamasiTheme {
                SayfaGecisleri()
            }
        }
    }
}
@Composable
fun SayfaGecisleri(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination ="Anasayfa") {
        composable("Anasayfa"){
            Anasayfa(navController = navController)
        }
        composable("DetaySayfa/{yemek}", arguments = listOf(
            navArgument("yemek"){type = NavType.StringType}
        )){ //yemek isimli nesne transferi olacak
            // nesne transferi bu şekilde oluyor
            // -----1
            val json = it.arguments?.getString("yemek")
            val yemek = Gson().fromJson(json,Yemekler::class.java)
            DetaySayfa(yemek = yemek)
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Anasayfa(navController: NavController) {
    val yemekListesi = remember {
        mutableStateListOf<Yemekler>()
    }
    // app çalışınca direkt çalışır
    LaunchedEffect(key1 = true) {
        val y1 = Yemekler(1, "Köfte", "kofte", 15
        )
        val y2 = Yemekler(
            yemek_id = 2, "Ayran", "ayran",  2
        )
        val y3 = Yemekler(
            yemek_id = 3, "Fanta",  "fanta",  3
        )
        val y4 = Yemekler(
            yemek_id = 4,  "Makarna",  "makarna", 10
        )
        val y5 = Yemekler(
            yemek_id = 5, "Kadayıf", "kadayif", 25
        )
        val y6 = Yemekler(
            yemek_id = 6, "Baklava", "baklava",  30
        )

        yemekListesi.add(y1)
        yemekListesi.add(y2)
        yemekListesi.add(y3)
        yemekListesi.add(y4)
        yemekListesi.add(y5)
        yemekListesi.add(y6)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Yemekler Uygulaması") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(id = R.color.black),
                    titleContentColor = colorResource(id = R.color.white)
                )
            )
        },
        content = {
            LazyColumn {
              items(
                  count = yemekListesi.count(),
                  itemContent = {
                      val yemek = yemekListesi[it]
                      Card(modifier = Modifier
                          .fillMaxWidth()
                          .padding(all = 10.dp))
                      {
                        Row(modifier = Modifier.clickable {
                            //--------2
                            val yemekJson = Gson().toJson(yemek)
                            navController.navigate("DetaySayfa/$yemekJson")
                        })//tıklama için
                        {//tasarım için
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()) {
                                val activity = (LocalContext.current as Activity)
                                Image(bitmap = ImageBitmap.imageResource(id = activity.resources.getIdentifier(
                                    yemek.yemek_resim_adi,"drawable",activity.packageName
                                )),
                                    contentDescription ="", modifier = Modifier.size(100.dp))
                                Row(verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    Column(verticalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier.fillMaxHeight()) {
                                        Text(text = yemek.yemek_adi, fontSize = 20.sp)
                                        Text(text ="${yemek.yemek_fiyat} tl", color = Color.Blue)
                                    }
                                    Icon(painter = painterResource(id = R.drawable.arrow_resim) , contentDescription = "")
                                }
                            }
                        }
                      }
                  }
              )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YemeklerUygulamasiTheme {

    }
}