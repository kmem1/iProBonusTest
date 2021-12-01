package com.progressterra.ipbtest

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.progressterra.api_module.common.RequestResult
import com.progressterra.api_module.data.repository.IPBRepository
import com.progressterra.view_module.BonusesViewFragment
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.net.NetworkInfo

import android.net.ConnectivityManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            if (internetConnection()) {
                lifecycleScope.launchWhenCreated {
                    findViewById<ProgressBar>(R.id.loading_pb).visibility = View.VISIBLE

                    val accessTokenResult = IPBRepository(BuildConfig.ACCESS_KEY).getAccessToken(
                        BuildConfig.CLIENT_ID,
                        BuildConfig.DEVICE_ID
                    )

                    if (accessTokenResult is RequestResult.Success) {
                        createFragment(getBonusesData(accessTokenResult.data))
                    } else {
                        throw IOException()
                    }

                    findViewById<ProgressBar>(R.id.loading_pb).visibility = View.GONE
                }
            } else {
                Toast.makeText(baseContext, "Error while connecting", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(baseContext, "Error while connecting", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getBonusesData(accessToken: String): BonusesData = coroutineScope {
        var currentBonusesAmount = 0
        var burningBonusesAmount = 0
        var dateBurning = ""

        awaitAll(
            async {
                val currentBonusesResult =
                    IPBRepository(BuildConfig.ACCESS_KEY).getCurrentBonuses(accessToken)

                if (currentBonusesResult is RequestResult.Success) {
                    currentBonusesAmount = currentBonusesResult.data.toInt()
                } else {
                    throw IOException()
                }
            },
            async {
                val burningBonusesResult =
                    IPBRepository(BuildConfig.ACCESS_KEY).getBurningBonuses(accessToken)

                if (burningBonusesResult is RequestResult.Success) {
                    burningBonusesAmount = burningBonusesResult.data.toInt()
                } else {
                    throw IOException()
                }
            },
            async {
                val dateBurningResult =
                    IPBRepository(BuildConfig.ACCESS_KEY).getBonusesBurningDate(accessToken)

                if (dateBurningResult is RequestResult.Success) {
                    dateBurning = SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.getDefault()
                    ).format(dateBurningResult.data)
                } else {
                    throw IOException()
                }
            }
        )


        BonusesData(currentBonusesAmount, burningBonusesAmount, dateBurning)
    }

    private fun createFragment(bonusesData: BonusesData) {
        val fragment = BonusesViewFragment.newInstance(
            bonusesData.currentBonusesAmount,
            bonusesData.burningBonusesAmount,
            bonusesData.dateBurning
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, "frag")
            .setReorderingAllowed(true)
            .commit()
    }

    private fun internetConnection(): Boolean {
        //Check if connected to internet, output accordingly
        val cm =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }

    private data class BonusesData(
        val currentBonusesAmount: Int,
        val burningBonusesAmount: Int,
        val dateBurning: String
    )
}