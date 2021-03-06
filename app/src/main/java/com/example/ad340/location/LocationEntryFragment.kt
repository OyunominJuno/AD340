package com.example.ad340.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ad340.Location
import com.example.ad340.LocationRepository

import com.example.ad340.R

/**
 * A simple [Fragment] subclass.
 */
class LocationEntryFragment : Fragment() {

    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationRepository = LocationRepository(requireContext())

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location_entry, container, false)

        //update UI, get reference

        val editTextZipCode : EditText = view.findViewById(R.id.editTextZipCode)
        val enterButtonZipCode : Button = view.findViewById(R.id.buttonZipCode)

        enterButtonZipCode.setOnClickListener {
            val textZipCode : String = editTextZipCode.text.toString()
            if (textZipCode.length != 5) {
                Toast.makeText(requireContext(), R.string.invalid_zip_code, Toast.LENGTH_SHORT).show()
            } else {
                locationRepository.saveLocation(Location.Zipcode(textZipCode))
                findNavController().navigateUp()
            }
        }

        return view
    }

}
