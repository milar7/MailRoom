package com.example.mailroom.ui.mails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.databinding.MailsFragmentBinding
import com.example.mailroom.ui.users.UserListAdapter
import com.example.mailroom.util.InjectorUtil

class MailsFragment : Fragment(), MailListAdapter.Interaction {

    companion object {
        fun newInstance() = MailsFragment()
    }

    private lateinit var viewModel: MailsViewModel
    private lateinit var binding: MailsFragmentBinding
    private lateinit var mailsAdapter: MailListAdapter

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


        binding.rvAllMails.apply {
            layoutManager= LinearLayoutManager(requireContext())
            mailsAdapter= MailListAdapter(this@MailsFragment)
            adapter=mailsAdapter
        }
        viewModel.getMails().observe(viewLifecycleOwner, Observer {
mailsAdapter.submitList(it)
        })

        binding.fabNewMail.setOnClickListener {
            findNavController().navigate(MailsFragmentDirections.actionMailsFragmentToNewMailFragment())
        }
    }

    override fun onItemSelected(position: Int, item: Mail) {
        findNavController().navigate(MailsFragmentDirections.actionMailsFragmentToDetailMailFragment(item))

    }
}