package com.example.android.politicalpreparedness.features.election

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.CustomApplication
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import timber.log.Timber

class VoterInfoFragment : Fragment() {

    private lateinit var application: CustomApplication
    private lateinit var voterViewModel: VoterInfoViewModel
    private lateinit var binding:FragmentVoterInfoBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Add ViewModel values and create ViewModel
        binding = FragmentVoterInfoBinding.inflate(layoutInflater)
        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        val voterInfoFragmentArgs by navArgs<VoterInfoFragmentArgs>()
        val voterInfoViewModelFactory = VoterInfoViewModelFactory(application.electionRepository, voterInfoFragmentArgs)

        voterViewModel = ViewModelProvider(this, voterInfoViewModelFactory).get(VoterInfoViewModel::class.java)

        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    //TODO: Create method to load URL intents

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        voterViewModel.getVoterInfo()
        voterViewModel.voterInfoData.observe(viewLifecycleOwner, Observer {
            voterViewModel.updateData()
            binding.electionName.title = it.election.name

            Timber.d("voter info: ${it}")
        })
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        application = requireActivity().application as CustomApplication
    }
}