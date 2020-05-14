package com.example.projectwork.dictionary

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectwork.R
import com.example.projectwork.base_list.Bindable
import com.example.projectwork.base_list.ListAdapter
import com.example.projectwork.base_list.holder_creators.Creator
import com.example.projectwork.base_list.holder_creators.SimpleCreator
import com.example.projectwork.database.PolyglotData
import com.example.projectwork.databinding.DictionaryItemWithBindingBinding
import com.example.projectwork.settings.CurrentLanguageData
import kotlinx.android.synthetic.main.item_without_binding.view.*


//если тебе нужна обработка нажатия, то лучше передавать обработчик через конструктор таким образом
class DictionaryAdapter(private val onClickAction : (CurrentLanguageData) -> Unit) : ListAdapter<CurrentLanguageData>() {
    //если у тебя в лаяуте item'а списка есть DataBinding, тогда используй BindingCreator
//    override val creator: Creator<RecyclerView.ViewHolder> =
//        //Передается конструктор класса, generic параметры это классы Binding и ViewHolder
//        object : BindingCreator<DictionaryItemWithBindingBinding, BindingDictionaryViewHolder>(::BindingDictionaryViewHolder){
//        override val holderLayout: Int
//            get() = R.layout.dictionary_item_with_binding
//    }

    // если нету databindinga тогда так
    override val creator: Creator<RecyclerView.ViewHolder> =
        //Передается конструктор класса, generic параметры это классы Binding и ViewHolder
        object : SimpleCreator<DictionaryViewHolder>(::DictionaryViewHolder){
            override val holderLayout: Int
                get() = R.layout.item_without_binding
        }

    /**
     * Классы ниже отвечают за отрисовку и поведение каждого элемента списка,
     * в них происходит связывание данных с интерфейсом
     */
    //c databindingom
    inner class BindingDictionaryViewHolder(private val binding : DictionaryItemWithBindingBinding) : RecyclerView.ViewHolder(binding.root),
        Bindable<CurrentLanguageData>{
        //в этом методе связываем наш лайоут с данными
        override fun bind(item: CurrentLanguageData) {
            binding.wordData = item
            binding.button.setOnClickListener {
                onClickAction(item)
            }
        }

    }

    //без databindingа
    inner class DictionaryViewHolder(private val view : View) : RecyclerView.ViewHolder(view), Bindable<CurrentLanguageData>{
        override fun bind(item: CurrentLanguageData) {
            view.apply {
                button1.text = item.word
                button1.setOnClickListener {
                    onClickAction(item)
                }
            }
        }

    }
}