package com.example.projectwork.dictionary

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectwork.App

import com.example.projectwork.R
import com.example.projectwork.base_list.ListAdapter
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.oldwordrepeat.OldWordRepeatFragment
import com.example.projectwork.settings.CurrentLanguageData
import kotlinx.android.synthetic.main.dictionary_fragment.*
import kotlinx.coroutines.withContext

class DictionaryFragment : Fragment() {

    companion object {
        fun newInstance() = DictionaryFragment()
    }

    private lateinit var viewModel: DictionaryViewModel
    private val adapter : ListAdapter<CurrentLanguageData> = DictionaryAdapter{
        //обработку нажатий на элемент списка лучше делать из фрагмента
        val bundle = bundleOf(OldWordRepeatFragment.WORD_ID to it.wordId)
        view?.findNavController()?.navigate(R.id.action_dictionaryFragment_to_oldWordRepeatFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
    }

    //передаем в RecyclerView, который у нас в лаяуте прописан адаптер, который хранит у нас список итемов и менеджер(если нужно будет расскажу)
    private fun setupList(){
        words_rv.layoutManager = LinearLayoutManager(context)
        words_rv.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DictionaryViewModel::class.java)

        //val linDict = LinearLayout(this!)
        //подписываемся на лайвдату и при обновлении она будет обновлять список

//        viewModel.getValues()
        viewModel.okWords.observe(viewLifecycleOwner){
            adapter.setList(it)
            Log.d("Dictionary", "okWords = $it")
        }
//        adapter.setList(viewModel.okWords.value!!)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { findNavController().navigate(R.id.action_dictionaryFragment_to_oldWordsMenuFragment) }
        }
    }

}
