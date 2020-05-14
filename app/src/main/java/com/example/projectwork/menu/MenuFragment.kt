package com.example.projectwork.menu

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.projectwork.R
import com.example.projectwork.databinding.MenuFragmentBinding
import com.google.android.material.snackbar.Snackbar
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.menu_fragment.*


class MenuFragment : Fragment() {

    companion object {
        fun newInstance() = MenuFragment()
    }

    private val viewModel: MenuViewModel by viewModels()
    //Обьявления обьекта для binding
    private lateinit var binding : MenuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //вот так он создается. Именно в этом методе!!!
        binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //а именно в этом методе в него передаются параметры
        binding.apply {
            //передаем viewModel, чтобы из нее бралось нужное поле oldWordsButtonsVisible
            menuViewModel = viewModel
            //также передаем lifecycleOwner, потому что, чтобы у тебя данные сами обновлялись, а не один раз и все, то liveDat'е нужен lifecycleOwner
            //лучше не забывай его передавать, если у тебя во viewModel liveData, потому что этот баг искать долго
            binding.lifecycleOwner = viewLifecycleOwner
            newWords.setOnClickListener {
                if (!(viewModel.isNewWords || viewModel.isOldWords)) {
                    viewModel.currentLanguageIsNotReadySnackbar()
                } else if (!viewModel.isNewWords && viewModel.isOldWords) {
                    viewModel.wordsAreNotReadySnackbar()
                } else {
                    view?.let { findNavController().navigate(R.id.action_menuFragment_to_newWordsFragment) }
                }
            }
            oldWords.setOnClickListener { view?.let { findNavController().navigate(R.id.action_menuFragment_to_oldWordsMenuFragment) } }
            stats.setOnClickListener {
                if (!(viewModel.isNewWords || viewModel.isOldWords)) {
                    viewModel.currentLanguageIsNotReadySnackbar()
                } else {
                    view?.let { findNavController().navigate(R.id.action_menuFragment_to_statsFragment) }
                }
            }
            settings.setOnClickListener {
                if (!viewModel.isLanguages) {
                    viewModel.languagesAreNotReadySnackbar()
                } else {
                    view?.let { findNavController().navigate(R.id.action_menuFragment_to_appSettingsFragment) }
                }
            }
        }
        /*   Photography
        viewModel.userPhoto.observe(this){
            url -> Picasso.get().load(url).into(userPhoto)
        }
        */

        viewModel.languages.observe(viewLifecycleOwner){
            Log.d("Menu", "List of languages = $it")
        }
        viewModel.word.observe(viewLifecycleOwner){
            Log.d("Menu", "Word = $it")
        }

        viewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Невозможно открыть: " + viewModel.snackbarString.value,
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                viewModel.doneShowingSnackbar()
            }
        }
//        viewModel.insertsCount.observe(viewLifecycleOwner) {
//            Log.d("Menu", "inserts = $it")
//        }


        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { //findNavController().navigate(R.id.action_menuFragment_to_exitScreenFragment)
                System.exit((-1))
            }
        }
    }
}
