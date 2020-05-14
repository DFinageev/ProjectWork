package com.example.projectwork.settings

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectwork.R
import com.example.projectwork.base_list.Bindable
import com.example.projectwork.base_list.ListAdapter
import com.example.projectwork.base_list.holder_creators.Creator
import com.example.projectwork.base_list.holder_creators.SimpleCreator
import kotlinx.android.synthetic.main.app_settings_item_with_binding.view.*
import kotlin.math.roundToInt


//если тебе нужна обработка нажатия, то лучше передавать обработчик через конструктор таким образом
class AppSettingsAdapter(private val onClickAction : (LanguageData) -> Unit) : ListAdapter<LanguageData>() {
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
        object : SimpleCreator<AppSettingsViewHolder>(::AppSettingsViewHolder){
            override val holderLayout: Int
                get() = R.layout.app_settings_item_with_binding
        }

    /**
     * Классы ниже отвечают за отрисовку и поведение каждого элемента списка,
     * в них происходит связывание данных с интерфейсом
     */
    //c databindingom
//    inner class BindingAppSettingsViewHolder(private val binding : AppSettingsItemWithBindingBinding) : RecyclerView.ViewHolder(binding.root),
//        Bindable<LanguageData>{
//        //в этом методе связываем наш лайоут с данными
//        override fun bind(item: LanguageData) {
//            binding.languageData = item
//            binding.button.setOnClickListener {
//                onClickAction(item)
//            }
//        }
//
//    }

    //без databindingа
    inner class AppSettingsViewHolder(private val view : View) : RecyclerView.ViewHolder(view), Bindable<LanguageData>{
        override fun bind(item: LanguageData) {
            view.apply {
                //language_tv.text = item.language
                button.text = item.language
//                var metrics = DisplayMetrics()
//
//                display.getMetrics(metrics)
//                button.width = (metrics.widthPixels.toDouble() * 0.8).roundToInt()
                var size = Point()
                itemView.apply {
                    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    windowManager.defaultDisplay.getSize(size)
                }
                button.width = (size.x.toDouble() * 0.8).roundToInt()

                button.setOnClickListener {
                    onClickAction(item)

                }
            }
        }

    }
}