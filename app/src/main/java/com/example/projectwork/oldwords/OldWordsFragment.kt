package com.example.projectwork.oldwords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.projectwork.R
import com.example.projectwork.databinding.OldWordsFragmentBinding
import kotlinx.android.synthetic.main.old_words_fragment.*


class OldWordsFragment : Fragment() {

    companion object {
        fun newInstance() = OldWordsFragment()
    }

    private val viewModel: OldWordsViewModel by viewModels()

    private lateinit var binding : OldWordsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OldWordsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {

            oldWordsViewModel = viewModel
            binding.lifecycleOwner = lifecycleOwner
            checkButton.setOnClickListener { view?.let { findNavController().navigate(R.id.action_oldWordsFragment_to_oldWordsMenuFragment) } }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { findNavController().navigate(R.id.action_oldWordsFragment_to_oldWordsMenuFragment) }
        }
    }

}
