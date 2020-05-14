package com.example.projectwork.oldwordsmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController

import com.example.projectwork.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.old_words_menu_fragment.*

class OldWordsMenuFragment : Fragment() {

    companion object {
        fun newInstance() = OldWordsMenuFragment()
    }

    private val viewModel: OldWordsMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.old_words_menu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dictionaryButton.setOnClickListener {
            if (!viewModel.isOldWords) {
                viewModel.startShowingSnackbar()
            } else {
                view?.let { findNavController().navigate(R.id.action_oldWordsMenuFragment_to_dictionaryFragment) }
            }
        }
        oldWordsButton.setOnClickListener {
            if (!viewModel.isOldWords) {
                viewModel.startShowingSnackbar()
            } else {
                view?.let { findNavController().navigate(R.id.action_oldWordsMenuFragment_to_oldWordsFragment) }
            }
        }
        constructButton.setOnClickListener {
            if (!viewModel.isOldWords) {
                viewModel.startShowingSnackbar()
            } else {
                view?.let { findNavController().navigate(R.id.action_oldWordsMenuFragment_to_oldWordsConstructFragment) }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { findNavController().navigate(R.id.action_oldWordsMenuFragment_to_menuFragment) }
        }


        viewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "В разделе нет слов, либо язык ещё до конца не загрузился",
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                viewModel.doneShowingSnackbar()
            }
        }
    }


}
