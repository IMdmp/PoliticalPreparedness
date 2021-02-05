package com.example.android.politicalpreparedness.features.election

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.CustomApplication
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.features.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.features.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import timber.log.Timber
import java.lang.Exception

class ElectionsFragment: BaseFragment(), ElectionListener {

    private lateinit var viewModel: ElectionsViewModel
    private lateinit var adapter:ElectionListAdapter
    private lateinit var binding:FragmentElectionBinding
    private lateinit var customApplication: CustomApplication

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentElectionBinding.inflate(inflater)
        val viewModelFactory  = ElectionsViewModelFactory(customApplication.electionRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ElectionsViewModel::class.java)

        binding.electionViewModel = viewModel
        binding.lifecycleOwner = this

        //TODO: Link elections to voter info
        setupAdapter()

        //TODO: Populate recycler adapters

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refresh()
    }

    private fun setupAdapter() {
        adapter = ElectionListAdapter(this)
        binding.rvUpcomingElections.adapter = adapter
        binding.rvUpcomingElections.layoutManager = LinearLayoutManager(requireContext())
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override val _viewModel: BaseViewModel
        get() = viewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        customApplication = requireActivity().application as CustomApplication
    }

    override fun onElectionSelected(election: Election) {
        viewModel.navigationCommand.value =
                NavigationCommand.To(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id,election.division))
    }
    //TODO: Refresh adapters when fragment loads

}