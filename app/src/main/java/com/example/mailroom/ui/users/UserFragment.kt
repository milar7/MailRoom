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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.User
import com.example.mailroom.databinding.UserFragmentBinding
import com.example.mailroom.util.InjectorUtil
import com.example.mailroom.util.dismissKeyboard
import kotlinx.android.synthetic.main.fragment_new_user.*

class UserFragment : Fragment(), UserListAdapter.Interaction {


    private lateinit var viewModel: UserViewModel
    private lateinit var binding: UserFragmentBinding
    private lateinit var userAdapter:UserListAdapter

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

        binding.rvUsers.apply {
            layoutManager= LinearLayoutManager(requireContext())
            userAdapter= UserListAdapter(this@UserFragment)
            adapter=userAdapter
        }
        viewModel.getUsers().observe(viewLifecycleOwner, Observer {
            userAdapter.submitList(it)
        })


        var role = ""
        binding.fabNewUser.setOnClickListener {
           // findNavController().navigate(UserFragmentDirections.actionUserFragmentToNewUserFragment())
            val dialog = MaterialDialog(requireContext())
                .show {
                    customView(R.layout.fragment_new_user)
                    cancelable(false)
                    cancelOnTouchOutside(false)
                }
            dialog.btn_cancel_user.setOnClickListener {
                dialog.dismiss()
                dismissKeyboard()
            }
            dialog.btn_submit_user.setOnClickListener {
                if (dialog.text_name.text.toString().trim().isEmpty()) {
                    dialog.Input_name.error="can not be empty"
                return@setOnClickListener
                } else dialog.Input_name.error=null

                viewModel.addNewUser(User(name =dialog.text_name.text.toString().trim(),
                role = role))



                dialog.dismiss()
                dismissKeyboard()
            }



            val spinner = dialog.roles_spinner
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
                    role=parent?.getItemAtPosition(position) as String
                }

            }

        }

    }

    override fun onItemSelected(position: Int, item: User) {
        Toast.makeText(requireContext(),item.name,Toast.LENGTH_SHORT).show()
    }


}