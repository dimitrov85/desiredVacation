package com.example.desiredvacations

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_detailed.view.*
import kotlinx.android.synthetic.main.item_vacation.view.*

class VacationAdapter(
    val vacations: MutableList<Vacation>,
    private val context: Context,
    private val viewModel: ItemViewModel

) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {

    class VacationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        return VacationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_vacation,
                parent,
                false
            )
        )
    }

    fun addVacation(vacation: Vacation) {
        vacations.add(vacation)
        notifyItemInserted(vacations.size - 1)
    }

    private fun deleteVacation(vacation: Vacation) {
        vacations.remove(vacation)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        val curVacation = vacations[position]

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.purple_200
                )
            )
        }

        holder.itemView.apply {
            itemName.text = curVacation.name
            itemLocation.text = curVacation.location

            itemVacationView.setOnLongClickListener {
                deleteVacation(vacations[position])
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()

                val databaseHandler: DataBaseHandler = DataBaseHandler(context)
                val status = databaseHandler.deleteData(Vacation(curVacation.vacationId, "", "", "", "", ""))

                return@setOnLongClickListener true
            }

            btnDetailedVacation.setOnClickListener {
                viewModel.chosenVacation = curVacation.vacationId

                findNavController().navigate(R.id.action_mainFragment_to_detailedFragment)
                Log.e("tag", "chosen $curVacation")
            }

//            btnUpdateVacation.setOnClickListener {
//
//                findNavController().navigate(R.id.action_mainFragment_to_updateFragment)
//            }
        }

    }

    override fun getItemCount(): Int {
        return vacations.size
    }

}