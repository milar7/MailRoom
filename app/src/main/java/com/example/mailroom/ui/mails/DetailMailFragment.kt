package com.example.mailroom.ui.mails

import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.data.entity.User
import com.example.mailroom.databinding.FragmentDetailMailBinding
import com.example.mailroom.ui.users.UserListAdapter
import com.example.mailroom.util.Constans
import com.example.mailroom.util.CurrentFragment
import com.example.mailroom.util.InjectorUtil
import kotlinx.android.synthetic.main.layout_recive_or_send.*
import java.text.DateFormat


class DetailMailFragment : Fragment(), UserListAdapter.Interaction {

    private lateinit var binding: FragmentDetailMailBinding
    private lateinit var viewModel: MailsViewModel
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var sender: User
    private lateinit var reciver: User
    private lateinit var  uri : Uri


    val args: DetailMailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr= Constans.NOTHOME
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_mail, container, false)
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


//        binding.tv.text=args.mail.title
        var calendar: Calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar.getInstance()
        } else {
            TODO("VERSION.SDK_INT < N")
        }
            uri=args.mail.uri.toUri()
        binding.apply {
            tvDate.text = DateFormat.getDateInstance().format(args.mail.sendDate!!)
            textTitle.setText(args.mail.title)
           textMail.setText(args.mail.text)
            sender=args.mail.sender
            reciver=args.mail.receiver
           tvSender.text= "Send By  :${sender.name}"
            binding.tvGiver.text= "Receive By : ${reciver.name}"
        }
        Glide.with(requireActivity()).load(args.mail.uri.toUri()).into(binding.ivImage)

        binding.ivImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT;
            startActivityForResult(
                Intent.createChooser(intent,
                "Select image"),111)
        }

        binding.rvSenderSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            userListAdapter = UserListAdapter(this@DetailMailFragment)
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
        var type =args.mail.type
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
                //type = parent?.getItemAtPosition(position) as String
            }

        }
        binding.fabUpdateMail.setOnClickListener {
            viewModel.addMail(
                Mail(
                    mailId = args.mail.mailId,
                    title = binding.textTitle.text.toString(),
                    text = binding.textMail.text.toString(),
                    type = type,
                    sendDate = args.mail.sendDate,
                    userSenderId = sender.userId!!,
                    sender = sender,
                    receiver = reciver,
                    uri = uri.toString()
                )
            )
            Toast.makeText(requireContext(), "updated!", Toast.LENGTH_SHORT).show()

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode ==111) {
            uri = data?.data!!
            Glide.with(requireActivity()).load(uri).into(binding.ivImage)
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