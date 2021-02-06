package com.example.android.politicalpreparedness.features.election

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.CustomApplication
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.features.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.features.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment: BaseFragment(), ElectionListener {

    private lateinit var viewModel: ElectionsViewModel
    private lateinit var upcomingAdapter:ElectionListAdapter
    private lateinit var savedAdapter:ElectionListAdapter
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
        upcomingAdapter = ElectionListAdapter(this)
        binding.rvUpcomingElections.adapter = upcomingAdapter
        binding.rvUpcomingElections.layoutManager = LinearLayoutManager(requireContext())
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            upcomingAdapter.submitList(it)
        })
        savedAdapter = ElectionListAdapter(this)
        binding.rvSavedElections.adapter= savedAdapter
        binding.rvSavedElections.layoutManager = LinearLayoutManager(requireContext())
        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            savedAdapter.submitList(it)
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