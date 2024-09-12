package ir.ehsan.asmrweather.screens

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.screens.HomeViewModel
import ir.ehsan.asmrweather.models.BaseModel
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.ehsan.asmrweather.models.Location

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val locations by viewModel.locations.collectAsState()
    val (city, setCity) = remember {
        mutableStateOf("")
    }

    LaunchedEffect(city) {
        delay(500)
        if (city.isNotEmpty()) {
            viewModel.searchLocation(city)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome To Weather App",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            TextField(modifier = Modifier.fillMaxWidth(), value = city, onValueChange = {
                setCity(it)
            }, colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                focusedPlaceholderColor = Color.Gray
            ),
                placeholder = {
                    Text(text = "City")
                }

            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        AnimatedVisibility(
            visible = locations is BaseModel.Success,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(text = "Choose Your City", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                when (val data = locations) {
                    is BaseModel.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(50.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(data.data) { location ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .clip(
                                            RoundedCornerShape(8.dp)
                                        )
                                        .background(MaterialTheme.colorScheme.secondary)
                                        .clickable {
                                            navController.navigate("weather/${location.key}/${location.englishName}/${location.country.englishName}")
                                        }
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            location.englishName,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = location.country.englishName,
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
        AnimatedVisibility(
            visible = locations is BaseModel.Loading, enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            CircularProgressIndicator(color = Color.White)
        }

    }
}