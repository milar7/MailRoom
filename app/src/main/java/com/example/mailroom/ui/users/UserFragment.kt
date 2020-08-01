package com.example.mailroom.ui.users

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
import com.example.mailroom.databinding.UserFragmentBinding
import com.example.mailroom.ui.mails.MailsFragmentDirections
import com.example.mailroom.util.InjectorUtil

class UserFragment : Fragment() {


    private lateinit var viewModel: UserViewModel
    private lateinit var binding: UserFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


                 binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment, container, false)
                 binding.lifecycleOwner = this
                 return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mailRepository = MailRepository(MailDatabase(requireContext()))
        val mailViewModelProviderFactory = InjectorUtil.UserViewModelProviderFactory(
            requireActivity().application,
            mailRepository
        )
        viewModel =
            ViewModelProvider(
                this,
                mailViewModelProviderFactory
            ).get(UserViewModel::class.java)
        binding.fabNewUser.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToNewUserFragment())

        }
    }


}