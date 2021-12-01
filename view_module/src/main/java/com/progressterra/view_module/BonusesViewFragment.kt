package com.progressterra.view_module

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class BonusesViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bonuses_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentBonusesAmount = arguments?.getInt(EXTRA_CURRENT_BONUSES_AMOUNT) ?: 0
        val burningBonusesAmount = arguments?.getInt(EXTRA_BURNING_BONUSES_AMOUNT) ?: 0
        val burningDate = arguments?.getString(EXTRA_BURNING_DATE) ?: "0.00"

        view.findViewById<TextView>(R.id.bonuses_amount_tv).text =
            resources.getString(R.string.bonuses_widget_bonuses_amount, currentBonusesAmount)

        view.findViewById<TextView>(R.id.burning_bonuses_amount_tv).text =
            resources.getString(R.string.bonuses_widget_bonuses_amount, burningBonusesAmount)

        view.findViewById<TextView>(R.id.burning_date_tv).text =
            resources.getString(R.string.bonuses_widget_date_burning, burningDate)

    }

    companion object {
        fun newInstance(
            currentBonusesAmount: Int,
            burningBonusesAmount: Int,
            burningDate: String
        ): BonusesViewFragment {
            val bundle = Bundle()
            bundle.putInt(EXTRA_CURRENT_BONUSES_AMOUNT, currentBonusesAmount)
            bundle.putInt(EXTRA_BURNING_BONUSES_AMOUNT, burningBonusesAmount)
            bundle.putString(EXTRA_BURNING_DATE, burningDate)

            val fragment = BonusesViewFragment()
            fragment.arguments = bundle

            return fragment
        }

        private const val EXTRA_CURRENT_BONUSES_AMOUNT = "cba_ex"
        private const val EXTRA_BURNING_BONUSES_AMOUNT = "bba_ex"
        private const val EXTRA_BURNING_DATE = "bd_ex"
    }
}