package com.example.mailroom.ui.mails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.databinding.MailsFragmentBinding
import com.example.mailroom.util.InjectorUtil

class MailsFragment : Fragment() {

    companion object {
        fun newInstance() = MailsFragment()
    }

    private lateinit var viewModel: MailsViewModel
    private lateinit var binding: MailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

                 binding = DataBindingUtil.inflate(inflater, R.layout.mails_fragment, container, false)
                 binding.lifecycleOwner = this
                 return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mailRepository = MailRepository(MailDatabase(requireContext()))
        val mailViewModelProviderFactory = InjectorUtil.MailViewModelProviderFactory(
            requireActivity().application,
            mailRepository
        )
        viewModel =
            ViewModelProvider(
                this,
                mailViewModelProviderFactory
            ).get(MailsViewModel::class.java)

        binding.fabNewMail.setOnClickListener {
            findNavController().navigate(MailsFragmentDirections.actionMailsFragmentToNewMailFragment())
        }
    }
}