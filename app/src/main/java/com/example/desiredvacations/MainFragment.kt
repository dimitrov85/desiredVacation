package com.example.desiredvacations

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_vacation.*

class MainFragment : Fragment() {
    private lateinit var vacationAdapter: VacationAdapter

    private val sharedViewModel: ItemViewModel by activityViewModels()

    private fun getItemsList(): ArrayList<Vacation> {
        val dataBaseHandler: DataBaseHandler = DataBaseHandler(requireContext())

        return dataBaseHandler.viewData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacationAdapter = VacationAdapter(getItemsList(), requireContext(), sharedViewModel)
        rvVacations.layoutManager = LinearLayoutManager(requireContext())
        rvVacations.adapter = vacationAdapter

        fun setupListOfDataIntoRecyclerView() {
            if (getItemsList().size > 0) {
                rvVacations.visibility = View.VISIBLE
                rvVacations.layoutManager = LinearLayoutManager(requireContext())
                rvVacations.adapter = vacationAdapter
            }
        }

        btnAddVacation.setOnClickListener {
            val vacationName = etVacationName.text.toString()
            val vacationLocation = etVacationLocation.text.toString()
            val vacationHotel = etHotel.text.toString()
            val vacationMoney = etNeededMoney.text.toString()
            val vacationDescription = etVacationDescription.text.toString()
            val vacation = Vacation(
                vacationAdapter.vacations.lastIndex + 1,
                vacationName,
                vacationLocation,
                vacationHotel,
//                vacationMoney,
                vacationDescription
            )
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            val databaseHandler: DataBaseHandler = DataBaseHandler(requireContext())

            val vacationExists: List<Vacation> =
                vacationAdapter.vacations.filter { it.name == vacationName }

            if (vacationName.isNotEmpty() && vacationExists.count() == 0 && vacationLocation.isNotEmpty()) {
                vacationAdapter.addVacation(vacation)

                val status = databaseHandler.insertData(vacation)

                if (status > -1) {
                    Toast.makeText(requireContext(), "Record saved", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Not saved", Toast.LENGTH_LONG).show()
                    Log.e("tag", "failedVacation ${vacationAdapter.vacations}")
                }

                setupListOfDataIntoRecyclerView()
            } else if (vacationExists.count() > 0){
                alertDialogBuilder.setTitle("This vacation name is not available").show()
            } else {
                alertDialogBuilder.setTitle("Please, insert valid data")
            }

            etVacationName.text.clear()
            etVacationLocation.text.clear()
            etHotel.text.clear()
            etNeededMoney.text.clear()
            etVacationDescription.text.clear()
        }
    }
}