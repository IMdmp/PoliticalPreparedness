package com.example.android.politicalpreparedness.features.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Add ViewModel values and create ViewModel
        val binding = FragmentVoterInfoBinding.inflate(layoutInflater)
        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        val voterInfoFragmentArgs by navArgs<VoterInfoFragmentArgs>()


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    //TODO: Create method to load URL intents

}