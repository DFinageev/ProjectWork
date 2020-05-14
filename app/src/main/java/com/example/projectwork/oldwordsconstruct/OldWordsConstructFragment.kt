package com.example.projectwork.oldwordsconstruct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.projectwork.BR.oldWordsConstructViewModel
import com.example.projectwork.R
import com.example.projectwork.databinding.OldWordsConstructFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.old_words_construct_fragment.*

class OldWordsConstructFragment : Fragment() {

    companion object {
        fun newInstance() = OldWordsConstructFragment()
    }

    private val viewModel: OldWordsConstructViewModel by viewModels()

    private lateinit var binding : OldWordsConstructFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OldWordsConstructFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {

            oldWordsConstructViewModel = viewModel
            binding.lifecycleOwner = lifecycleOwner

            checkButtonConstract.setOnClickListener {
                val answer = answer_text_constract.text.toString()
                viewModel.nextWord(answer)
            }

            viewModel.word.observe(viewLifecycleOwner) {
                if (viewModel.word.value?.wordId?.toInt() == -1) {
                    view?.let {findNavController().navigate(R.id.action_oldWordsConstructFragment_to_oldWordsMenuFragment)}
                }
                oldWordsConstructViewModel = viewModel
            }
            viewModel.wordText.observe(viewLifecycleOwner) {
                oldWordsConstructViewModel = viewModel
            }
        }


        viewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "" + viewModel.snackbarString.value,
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                viewModel.doneShowingSnackbar()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { findNavController().navigate(R.id.action_oldWordsConstructFragment_to_oldWordsMenuFragment) }
        }
    }

}
