//import android.content.Context
//import android.content.SharedPreferences
//import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Devices
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.android.billingclient.api.*
//import com.example.remindme.presentation.MainScreens.SelectDaysScreen
//import com.example.remindme.presentation.theme.RemindMeTheme
////import kotlin.coroutines.jvm.internal.CompletedContinuation.context
//
//class TrialManager(context: Context) {
//    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("trial_preferences", Context.MODE_PRIVATE)
//
//    // Key to store the trial start date
//    private val trialStartDateKey = "trial_start_date"
//
//    // Save the trial start date when the user starts the trial
//    fun setTrialStartDate() {
//        val editor = sharedPreferences.edit()
//        editor.putLong(trialStartDateKey, System.currentTimeMillis()) // Save current time in milliseconds
//        editor.apply()
//    }
//
//    // Get the trial start date
//    fun getTrialStartDate(): Long {
//        return sharedPreferences.getLong(trialStartDateKey, 0L)
//    }
//
//    // Check if the trial is expired (after 1 week)
//    fun isTrialExpired(): Boolean {
//        val trialStartDate = getTrialStartDate()
//        if (trialStartDate == 0L) return false // No trial started yet
//        val currentTime = System.currentTimeMillis()
//        val trialPeriodEnd = trialStartDate + (7 * 24 * 60 * 60 * 1000) // 1 week in milliseconds
//        return currentTime > trialPeriodEnd
//    }
//}
//
//@Composable
//fun SubscriptionScreen(
//    navController: NavController,
//    trialManager: TrialManager,
//    billingClient: BillingClient
//) {
//    val isTrialExpired = trialManager.isTrialExpired()
//    val isSubscribed = checkSubscriptionStatus(billingClient) // Assume this function checks if the user is subscribed
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Welcome to RemindMe",
//            style = MaterialTheme.typography.h5,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        if (isSubscribed) {
//            Text("You are on the PRO version!", style = MaterialTheme.typography.h6)
//            // Show the pro features UI
//            ShowProFeatures()
//        } else if (isTrialExpired) {
//            Text("Your trial has expired. Please subscribe to the PRO version for full access.")
//            // Show Upgrade button
//            UpgradeButton(billingClient)
//        } else {
//            Text("You are on the BASIC version with limited features. Try PRO for 1 week free!")
//            // Show Upgrade button
//            UpgradeButton(billingClient)
//        }
//    }
//}
//
//@Composable
//fun ShowProFeatures() {
//    Column(modifier = Modifier.fillMaxSize()) {
//        Text(text = "Access to premium features unlocked!", style = MaterialTheme.typography.bodySmall)
//        // Add premium features here (e.g., additional UI elements for pro users)
//    }
//}
//
//@Composable
//fun UpgradeButton(billingClient: BillingClient) {
//    Button(
//        onClick = { initiatePurchase(billingClient) },
//        modifier = Modifier.align(Alignment.CenterHorizontally)
//    ) {
//        Text("Upgrade to PRO")
//    }
//}
//
//fun initiatePurchase(billingClient: BillingClient) {
//    // Query available SKUs and initiate purchase process for the pro subscription
//    val skuList = listOf("your_pro_subscription_sku") // Replace with actual SKU of the PRO version
//    val params = SkuDetailsParams.newBuilder()
//        .setSkusList(skuList)
//        .setType(BillingClient.SkuType.SUBS)
//        .build()
//
//    billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
//        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
//            val skuDetails = skuDetailsList.firstOrNull()
//            if (skuDetails != null) {
//                val flowParams = BillingFlowParams.newBuilder()
//                    .setSkuDetails(skuDetails)
//                    .build()
//
//                billingClient.launchBillingFlow(context, flowParams)
//            }
//        }
//    }
//}
//
//fun checkSubscriptionStatus(billingClient: BillingClient): Boolean {
//    // Assume this function checks if the user has an active subscription
//    // This should query the BillingClient to check active purchases
//
//    val purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
//    return purchaseResult.purchasesList?.any { it.sku == "your_pro_subscription_sku" && it.purchaseState == Purchase.PurchaseState.PURCHASED } == true
//}
//@Preview(device = Devices.WEAR_OS_SQUARE, showSystemUi = true)
//@Composable
//fun SubscriptionScreen() {
//    RemindMeTheme {
//        SubscriptionScreen()
//    }
//}
