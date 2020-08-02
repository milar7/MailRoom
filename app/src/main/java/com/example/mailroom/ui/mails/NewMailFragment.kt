package com.example.mailroom.ui.mails

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.data.entity.User
import com.example.mailroom.databinding.FragmentNewMailBinding
import com.example.mailroom.ui.users.UserListAdapter
import com.example.mailroom.util.Constans
import com.example.mailroom.util.CurrentFragment
import com.example.mailroom.util.InjectorUtil
import kotlinx.android.synthetic.main.layout_recive_or_send.*
import java.text.DateFormat


class NewMailFragment : Fragment(), UserListAdapter.Interaction {


    private lateinit var binding: FragmentNewMailBinding
    private lateinit var viewModel: MailsViewModel
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var sender: User
    private lateinit var reciver: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr= Constans.NOTHOME

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_mail, container, false)
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
        var calendar: Calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar.getInstance()
        } else {
            TODO("VERSION.SDK_INT < N")
        }

        binding.tvDate.text = DateFormat.getDateInstance().format(calendar.time)
        binding.rvSenderSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            userListAdapter = UserListAdapter(this@NewMailFragment)
            adapter = userListAdapter
        }
        viewModel.getUsers().observe(viewLifecycleOwner, Observer {
            userListAdapter.submitList(it)
        })

        binding.textSender.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchUsersByName(s.toString()).observe(viewLifecycleOwner, Observer {
                    userListAdapter.submitList(it)
                })
            }
        })
        var type = ""
        val spinner = binding.spinnerMailType
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.mail_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                type = parent?.getItemAtPosition(position) as String
            }

        }
        binding.fabSendMail.setOnClickListener {
            viewModel.addMail(
                Mail(
                    title = binding.textTitle.text.toString(),
                    text = binding.textMail.text.toString(),
                    type = type,
                    sendDate = calendar.time,
                    userSenderId = sender.userId!!,
                    sender = sender,
                    receiver = reciver
                )
            )
            Toast.makeText(requireContext(), "sent!", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onItemSelected(position: Int, item: User) {
        val dialog = MaterialDialog(requireContext())
            .show {
                customView(R.layout.layout_recive_or_send)
                cancelable(false)
                cancelOnTouchOutside(false)
            }
        dialog.btn_sender.setOnClickListener {
            sender = item
            binding.tvSender.text= "Send By  :${item.name}"
            dialog.dismiss()
        }
        dialog.btn_receiver.setOnClickListener {
            reciver = item
            binding.tvGiver.text= "Receive By : ${item.name}"
            dialog.dismiss()
        }
    }

}

