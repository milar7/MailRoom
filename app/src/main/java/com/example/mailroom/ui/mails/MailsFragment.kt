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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.databinding.MailsFragmentBinding
import com.example.mailroom.ui.users.UserListAdapter
import com.example.mailroom.util.Constans
import com.example.mailroom.util.CurrentFragment
import com.example.mailroom.util.InjectorUtil
import com.google.android.material.snackbar.Snackbar

class MailsFragment : Fragment(), MailListAdapter.Interaction {


    private lateinit var viewModel: MailsViewModel
    private lateinit var mailsAdapter: MailListAdapter
    private lateinit var binding: MailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr=Constans.HOME

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
            layoutManager = LinearLayoutManager(requireContext())
            mailsAdapter = MailListAdapter(this@MailsFragment)
            adapter = mailsAdapter
        }
        viewModel.getMails().observe(viewLifecycleOwner, Observer {
            mailsAdapter.submitList(it)
        })

        binding.fabNewMail.setOnClickListener {
            findNavController().navigate(MailsFragmentDirections.actionMailsFragmentToNewMailFragment())
        }
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val mail =mailsAdapter.getItemAt(viewHolder.position)
                viewModel.deleteMail(mail)
                val snak = Snackbar.make(
                    viewHolder.itemView,
                    "Are you sure!!!",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("UNDO") {
                        viewModel.addMail(mail)
                    }
                snak.show()
            }

        }
ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvAllMails)
    }

    override fun onItemSelected(position: Int, item: Mail) {
        findNavController().navigate(
            MailsFragmentDirections.actionMailsFragmentToDetailMailFragment(
                item
            )
        )

    }



}