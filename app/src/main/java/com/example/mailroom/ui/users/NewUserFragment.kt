package com.example.mailroom.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.databinding.FragmentNewUserBinding
import com.example.mailroom.util.Constans
import com.example.mailroom.util.CurrentFragment
import com.example.mailroom.util.InjectorUtil

class NewUserFragment : Fragment() {

    private lateinit var binding: FragmentNewUserBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr= Constans.NOTHOME

                 binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_user, container, false)
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


        val spinner = binding.rolesSpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.roles_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(requireContext(),parent?.getItemAtPosition(position) as String,Toast.LENGTH_SHORT).show()
            }

        }
    }



}