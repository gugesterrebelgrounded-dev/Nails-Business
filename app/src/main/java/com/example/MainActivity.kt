package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KindzelBeautyApp()
            }
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, "home")
    object Book : BottomNavItem("Book", Icons.Filled.DateRange, "book")
    object WalkIn : BottomNavItem("Walk-ins", Icons.Filled.People, "walk_in")
    object Specials : BottomNavItem("Specials", Icons.Filled.Star, "specials")
    object Profile : BottomNavItem("Profile", Icons.Filled.Person, "profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KindzelBeautyApp() {
    val navController = rememberNavController()
    
    BoxWithConstraints {
        val isWideScreen = maxWidth > 600.dp
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "KINDZEL",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* Handle Notifications */ }) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                )
            },
            bottomBar = { 
                if (!isWideScreen) {
                    BottomNavigationBar(navController) 
                }
            },
            floatingActionButton = {
                if (!isWideScreen) {
                    FloatingActionButton(
                        onClick = { /* WhatsApp Action Mock */ },
                        containerColor = Color(0xFF25D366),
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Filled.Chat, contentDescription = "WhatsApp")
                    }
                }
            }
        ) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                if (isWideScreen) {
                    SideNavigationBar(navController)
                }
                
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Home.route,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(BottomNavItem.Home.route) { HomeScreen(navController) }
                    composable(BottomNavItem.Book.route) { BookingScreen() }
                    composable(BottomNavItem.WalkIn.route) { WalkInScreen() }
                    composable(BottomNavItem.Specials.route) { SpecialsScreen() }
                    composable(BottomNavItem.Profile.route) { ProfileScreen() }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Book,
        BottomNavItem.WalkIn,
        BottomNavItem.Specials,
        BottomNavItem.Profile
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Composable
fun SideNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Book,
        BottomNavItem.WalkIn,
        BottomNavItem.Specials,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationRail(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxHeight(),
        header = {
            FloatingActionButton(
                onClick = { /* WhatsApp Action Mock */ },
                containerColor = Color(0xFF25D366),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Icon(Icons.Filled.Chat, contentDescription = "WhatsApp")
            }
        }
    ) {
        items.forEach { item ->
            NavigationRailItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            // Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(SoftPink, RoseGold)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Beauty & Lifestyle",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate(BottomNavItem.Book.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = CharcoalGray),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Book Now", color = RoseGold)
                    }
                }
            }
        }

        item {
            Text("Popular Services", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val services = listOf("Manicure", "Pedicure", "Gel Overlay", "Nail Art", "Eyelashes")
                items(services) { service ->
                    ServiceCard(title = service)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Monthly Goal Bonus", fontWeight = FontWeight.Bold, color = RoseGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Visit us 3 times this month to unlock a VIP Crown status and 20% cashback!", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ServiceCard(title: String) {
    Card(
        modifier = Modifier.width(140.dp).height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(title, fontWeight = FontWeight.Medium, color = DeepRose)
        }
    }
}

@Composable
fun BookingScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Book Appointment", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Select Service") },
            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Select Date") },
            trailingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Select Technician (Optional)") },
            trailingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { /* Check Availability */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
        ) {
            Text("Find Availability")
        }
    }
}

@Composable
fun WalkInScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Walk-in Queue", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SoftPink)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Current Wait Time", color = DeepRose, fontWeight = FontWeight.Medium)
                Text("approx 25 mins", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = CharcoalGray)
            }
        }
        
        Text("Queue", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        
        val queue = listOf(
            Pair("Sarah M.", "Manicure"),
            Pair("Lindiwe T.", "Eyelashes"),
            Pair("VIP - John D.", "Men's Grooming")
        )
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(queue) { item ->
                ListItem(
                    headlineContent = { Text(item.first, fontWeight = FontWeight.Medium) },
                    supportingContent = { Text(item.second) },
                    leadingContent = { 
                        Icon(
                            Icons.Filled.Person, 
                            contentDescription = null,
                            tint = if (item.first.contains("VIP")) GoldAccent else RoseGold 
                        ) 
                    },
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surface)
                )
            }
        }
        
    }
}

@Composable
fun SpecialsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Weekly Specials", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(alpha = 0.2f)),
            border = border(1.dp, GoldAccent, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Mother's Day Special \uD83C\uDF38", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepRose)
                Spacer(modifier = Modifier.height(8.dp))
                Text("3 People Plain Manicure = R400", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = DeepRose)) {
                    Text("Claim Deal")
                }
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Unlimited Nail Art", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Valid until Friday. Walk-ins welcome.")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = {}) {
                    Text("Learn More", color = RoseGold)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(SoftPink),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Person, contentDescription = "Profile", modifier = Modifier.size(50.dp), tint = DeepRose)
        }
        
        Text("Amanda C.", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CharcoalGray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Loyalty Points", color = Color.Gray)
                    Text("1,250", color = GoldAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Filled.Star, contentDescription = "VIP", tint = GoldAccent, modifier = Modifier.size(32.dp))
            }
        }
        
        val options = listOf("My Bookings", "Favorite Styles", "Payment Methods", "Settings")
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(option, fontWeight = FontWeight.Medium)
                        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
                    }
                    if (option != options.last()) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

// Ensure the border function can be used locally or import handled if not native to compose foundation directly
fun border(width: androidx.compose.ui.unit.Dp, color: Color, shape: androidx.compose.ui.graphics.Shape) = androidx.compose.foundation.BorderStroke(width, color)

