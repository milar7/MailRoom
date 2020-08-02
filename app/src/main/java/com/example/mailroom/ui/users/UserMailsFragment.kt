package com.example.mailroom.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.mailroom.R
import com.example.mailroom.data.MailDatabase
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.databinding.FragmentUserMailsBinding
import com.example.mailroom.ui.mails.MailListAdapter
import com.example.mailroom.ui.mails.MailsViewModel
import com.example.mailroom.util.Constans
import com.example.mailroom.util.CurrentFragment
import com.example.mailroom.util.InjectorUtil
import kotlinx.android.synthetic.main.lauout_filter_user_mail.*
import java.text.DateFormat


class UserMailsFragment : Fragment(), MailListAdapter.Interaction {
    private lateinit var binding: FragmentUserMailsBinding
    private lateinit var viewModel: MailsViewModel
    private lateinit var mailsAdapter: MailListAdapter

    val args: UserMailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        CurrentFragment.curr= Constans.NOTHOME
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_mails, container, false)
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

        binding.tvName.text=args.item.name
        binding.tvRole.text=args.item.role

        binding.rvUserMails.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mailsAdapter = MailListAdapter(this@UserMailsFragment)
            adapter = mailsAdapter
        }
        viewModel.getUserMails(args.item.userId!!).observe(viewLifecycleOwner, Observer {
            mailsAdapter.submitList(it)
        })
        var isdesc=true
        var isSender=true


        binding.btnFilter.setOnClickListener {
            val dialog = MaterialDialog(requireContext())
                .show {
                    customView(R.layout.lauout_filter_user_mail)
                    cancelable(false)
                    cancelOnTouchOutside(false)
                }
                dialog.rg_date.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.rb_date_ase -> {
                            isdesc=false
                        }
                        R.id.rb_date_desc -> {
                            isdesc=true
                        }
                    }
                }
            dialog.rg_se_re.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.rb_sender -> {
                        isSender=true
                    }
                    R.id.rb_receiver -> {
                        isSender=false
                    }
                }
            }

            dialog.btn_submit_filter.setOnClickListener {

                    if (isSender && isdesc){
                        viewModel.getUserMailsSenderDesc(args.item.userId!!)
                            .observe(viewLifecycleOwner, Observer {
                                mailsAdapter.submitList(it)
                            })
                    }else if (isSender && !isdesc){
                        viewModel.getUserMailsSenderAsc(args.item.userId!!)
                            .observe(viewLifecycleOwner, Observer {
                                mailsAdapter.submitList(it)
                            })

                    }else if (!isSender && isdesc){
                        viewModel.getUserMailsReceiverDesc(args.item.userId!!)
                            .observe(viewLifecycleOwner, Observer {
                                mailsAdapter.submitList(it)
                            })

                    }else if (!isSender && !isdesc){
                        viewModel.getUserMailsReceiverAsc(args.item.userId!!)
                            .observe(viewLifecycleOwner, Observer {
                                mailsAdapter.submitList(it)
                            })
                    }



                dialog.dismiss()
            }

            dialog.btn_cancel_filter.setOnClickListener {
                viewModel.getUserMails(args.item.userId!!).observe(viewLifecycleOwner, Observer {
                    mailsAdapter.submitList(it)
                })
                dialog.dismiss()
            }
        }
    }

    override fun onItemSelected(position: Int, item: Mail) {

        findNavController().navigate(
            UserMailsFragmentDirections.actionUserMailsFragmentToDetailMailFragment(
                item
            )
        )
    }

}