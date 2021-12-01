package com.progressterra.ipbtest

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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenCreated {
            findViewById<ProgressBar>(R.id.loading_pb).visibility = View.VISIBLE

            val accessTokenResult = IPBRepository(BuildConfig.ACCESS_KEY).getAccessToken(
                BuildConfig.CLIENT_ID,
                BuildConfig.DEVICE_ID
            )

            if (accessTokenResult is RequestResult.Success) {
                try {
                    createFragment(getBonusesData(accessTokenResult.data))
                } catch (e: Exception) {
                    Toast.makeText(baseContext, "Error while connecting", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(baseContext, "Error while connecting", Toast.LENGTH_SHORT).show()
            }

            findViewById<ProgressBar>(R.id.loading_pb).visibility = View.GONE
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

    private data class BonusesData(
        val currentBonusesAmount: Int,
        val burningBonusesAmount: Int,
        val dateBurning: String
    )
}