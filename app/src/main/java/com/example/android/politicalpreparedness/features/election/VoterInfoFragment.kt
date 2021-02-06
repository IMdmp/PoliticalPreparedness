package com.example.android.politicalpreparedness.features.election

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.CustomApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import timber.log.Timber

class VoterInfoFragment : BaseFragment() {

    private lateinit var application: CustomApplication
    private lateinit var voterViewModel: VoterInfoViewModel
    private lateinit var binding: FragmentVoterInfoBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentVoterInfoBinding.inflate(layoutInflater)

        val voterInfoFragmentArgs by navArgs<VoterInfoFragmentArgs>()
        val voterInfoViewModelFactory = VoterInfoViewModelFactory(application.electionRepository, voterInfoFragmentArgs)

        voterViewModel = ViewModelProvider(this, voterInfoViewModelFactory).get(VoterInfoViewModel::class.java)
        binding.voterInfoViewModel = voterViewModel
        binding.lifecycleOwner = this

        return binding.root
    }


    fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        voterViewModel.getVoterInfo()

        voterViewModel.electionIsFavorite.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.followElectionButton.text = getString(R.string.unfollow_ellection)
            } else {
                binding.followElectionButton.text = getString(R.string.follow_election)
            }
        })

        voterViewModel.eventOpenUrl.observe(viewLifecycleOwner, Observer { url->
            if(url.isNotEmpty()){
                openWebPage(url)
                voterViewModel.onOpenUrlComplete()
            }
        })

        voterViewModel.showLoading.observe(viewLifecycleOwner, Observer { loading->
            if(loading){
                binding.pbLoading.visibility = View.VISIBLE
            }else{
               binding.pbLoading.visibility= View.GONE
            }
        })

    }

    override val _viewModel: BaseViewModel
        get() = voterViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        application = requireActivity().application as CustomApplication
    }
}