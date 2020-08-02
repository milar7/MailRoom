package com.example.mailroom.ui.mails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.databinding.FragmentSearchBinding
import com.example.mailroom.util.InjectorUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class SearchFragment : Fragment(), MailListAdapter.Interaction {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: MailsViewModel
    private lateinit var searchAdapter: MailListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

                 binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
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
        binding.rvSearchUserMails.apply {
            layoutManager = LinearLayoutManager(requireContext())
            searchAdapter= MailListAdapter(this@SearchFragment)
            adapter = searchAdapter
        }
        binding.textSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchUsersByName(s.toString()).observe(viewLifecycleOwner, Observer {
                  //  searchAdapter.submitList(it)
                })
            }
        })
    }

    override fun onItemSelected(position: Int, item: Mail) {

    }

}