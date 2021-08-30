package com.example.desiredvacations

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.fragment_main.*


class DetailedFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_detailed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacationAdapter = VacationAdapter(getItemsList(), requireContext(), sharedViewModel)

        Log.e("tag", "detailedVacation ${sharedViewModel.chosenVacation}")

        val curVacation = vacationAdapter.vacations.filter { it.vacationId == sharedViewModel.chosenVacation }

        viewDetailedName.text = curVacation[0].name
        viewDetailedLocation.text = curVacation[0].location
        viewDetailedHotel.text = curVacation[0].hotel

        btnUploadImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    // permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    // show popup to request runtime permission
                    ActivityCompat.requestPermissions(requireActivity(), permissions,
                        DetailedFragment.PERMISSION_CODE
                    )
                } else {
                    // permission already granted
                    pickImageFromGallery()
                }
            } else {
                //  system OS < Marshmallow
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        // Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    // permission from popup granted
                    pickImageFromGallery()
                } else {
                    // permission from popup denied
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView.setImageURI(data?.data)
        }
    }

}