package com.mcupurdija.is24_cs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.mcupurdija.is24_cs.databinding.FragmentRepoDetailsDialogBinding
import com.mcupurdija.is24_cs.networking.schema.RepoSchema
import java.text.SimpleDateFormat
import java.util.*


class RepoDetailsDialogFragment : DialogFragment() {

    private val ARG_PARAM1 = "param1"
    private lateinit var repoSchema: RepoSchema

    private var _binding: FragmentRepoDetailsDialogBinding? = null
    private val binding get() = _binding!!

    private val wsFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
    private val uiFormat = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())

    companion object {
        val TAG = "RepoDetailsDialogFragment"

        @JvmStatic
        fun newInstance(repoSchema: RepoSchema?) =
            RepoDetailsDialogFragment().apply {
                arguments = bundleOf(ARG_PARAM1 to repoSchema)
                }
            }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repoSchema = it.getSerializable(ARG_PARAM1) as RepoSchema
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
        binding.title.text = String.format("%s: %s", "Name", repoSchema.name.orEmpty())
        binding.language.text = String.format("%s: %s", "Language", repoSchema.language.orEmpty())
        binding.numberOfWatchers.text = String.format("%s: %s", "Number of watchers", repoSchema.watchers.toString().orEmpty())
        binding.description.text = String.format("%s: %s", "Description", repoSchema.description.orEmpty())
        binding.loginName.text = String.format("%s: %s", "Login name", repoSchema.ownerSchema.login.orEmpty())
        repoSchema.updatedAt.let { it ->
            val date = wsFormat.parse(it)
            date?.let {
                binding.repositoryUpdateDate.text =
                    String.format("%s: %s", "Updated at", uiFormat.format(it))
            }
        }
    }
}