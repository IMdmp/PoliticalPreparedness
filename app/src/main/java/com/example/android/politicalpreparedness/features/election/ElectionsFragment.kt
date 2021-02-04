package com.example.android.politicalpreparedness.features.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import timber.log.Timber

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    lateinit var electionViewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Add ViewModel values and create ViewModel
        val binding = FragmentElectionBinding.inflate(inflater)

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        GlobalScope.launch(Dispatchers.IO) {
            val electionList = CivicsApi.retrofitService.getElectionList().await()

            val voterInfo  = CivicsApi.retrofitService.getVoterInfo().await()

            val representatives = CivicsApi.retrofitService.getRepresentatives().await()


            Timber.d("check election list:")
        }

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}