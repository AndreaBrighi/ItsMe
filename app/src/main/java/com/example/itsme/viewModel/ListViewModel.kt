package com.example.itsme.viewModel

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.itsme.R
import com.example.itsme.db.BusinessCardRepository
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.Utilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*


class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val itemSelected: MutableLiveData<BusinessCardWithElements> =
        MutableLiveData<BusinessCardWithElements>()
    private val cardItems: LiveData<List<BusinessCardWithElements>?>
    private val repository: BusinessCardRepository = BusinessCardRepository(application)

    init {
        cardItems = repository.getCardItemList()!!
    }

    fun select(item: BusinessCardWithElements) {
        itemSelected.value = item
    }

    val selected: MutableLiveData<BusinessCardWithElements>
        get() = itemSelected

    fun getCardItems(): LiveData<List<BusinessCardWithElements>?> {
        return cardItems
    }

    fun getCardItemFromId(id: Long): LiveData<BusinessCardWithElements?>? {
        return repository.getCardItemFromId(id)
    }

    fun updateCard(item: BusinessCardWithElements) {
        repository.updateCardItem(item.card)
        val liveData = getCardItemFromId(item.card.uidC)
        var observer: Observer<BusinessCardWithElements?>? = null
        observer = Observer<BusinessCardWithElements?> { card ->
            item.elements.filter { it.uid == 0L }.forEach { repository.addElement(it) }
            val ids = item.elements.map { it.uid }
            for (el in card!!.elements) {
                if (el.uid in ids) {
                    repository.updateElement(item.elements.filter {el.uid==it.uid}[0])
                } else {
                    repository.removeElement(el)
                }
            }
            liveData!!.removeObserver(observer!!)
        }
        liveData?.observe(ProcessLifecycleOwner.get(), observer)
    }

    fun addCard(card: BusinessCardWithElements) {
        repository.addCardItem(card.card, card.elements)
    }

    fun deleteCard(card: BusinessCardWithElements) {
        repository.delete(card)
    }

    fun read(
        cardLive: MutableLiveData<BusinessCardWithElements>,
        contentUri: Uri,
        activity: AppCompatActivity
    ) {
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream: InputStream? =
                    activity.contentResolver.openInputStream(contentUri)
                val input: Reader = BufferedReader(InputStreamReader(inputStream))
                val text = input.readLines().joinToString("\n")
                cardLive.postValue(Utilities.readXMLString(text))
                //Log.v("tess", text)
                input.close()
                inputStream?.close()
            } catch (e: IOException) {
                        Toast.makeText(activity, activity.resources.getString(R.string.fail_file), Toast.LENGTH_SHORT)
                            .show()
            }
        }
    }

}