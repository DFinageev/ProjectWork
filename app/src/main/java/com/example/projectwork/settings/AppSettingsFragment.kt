package com.example.projectwork.settings

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectwork.App

import com.example.projectwork.R
import com.example.projectwork.base_list.ListAdapter
import kotlinx.android.synthetic.main.app_settings_fragment.*

class AppSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = AppSettingsFragment()
    }

    lateinit var myApp: App

    private lateinit var viewModel: AppSettingsViewModel
    private val adapter : ListAdapter<LanguageData> = AppSettingsAdapter{
        //обработку нажатий на элемент списка лучше делать из фрагмента
//        var myApp = App()
        myApp.currentLanguage = it.ID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.app_settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
    }

    //передаем в RecyclerView, который у нас в лаяуте прописан адаптер, который хранит у нас список итемов и менеджер(если нужно будет расскажу)
    private fun setupList(){
        languages_rv.layoutManager = LinearLayoutManager(context)
        languages_rv.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AppSettingsViewModel::class.java)

        //val linDict = LinearLayout(this!)
        //подписываемся на лайвдату и при обновлении она будет обновлять список
//        viewModel.languages.observe(viewLifecycleOwner){
//            adapter.setList(it)
//        }

        myApp = requireActivity().application as App

        adapter.setList(myApp.allLanguages)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { findNavController().navigate(R.id.action_appSettingsFragment_to_menuFragment) }
        }
    }
}
