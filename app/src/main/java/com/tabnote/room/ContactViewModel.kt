package com.tabnote.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(private val dao: ContactDao) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.DATE)

    private val _allContacts = dao.getAllContact().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _contacts = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.DATE -> dao.getContactByDate()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _hisContacts = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.DATE -> dao.getHisContactByDate()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ContactState())

    //只要这三个有一个变成新值就会执行以下代码
    val state = combine(_state, _sortType, _allContacts ,_hisContacts ,_contacts) { state, sortType , allContacts , hisContacts, contacts ->
        state.copy(
            allContacts = allContacts,
            hisContacts = hisContacts,
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }
            }

            is ContactEvent.SaveContact -> {

                val contact = Contact(
                    id = state.value.id,
                    date = state.value.date,
                    context = state.value.context,
                    link = state.value.link,
                    done = state.value.done
                )
                viewModelScope.launch {
                    dao.insertContact(contact)
                }

                _state.update {
                    it.copy(
                        id = "",
                        date = DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(LocalDate.now()),
                        context = "",
                        done = false,
                        link = "",
                    )
                }
            }

            is ContactEvent.UpsertContact -> {

                val contact = Contact(
                    id =  event.contact.id,
                    date = state.value.date,
                    context = state.value.context,
                    done = state.value.done,
                    link = state.value.link
                )
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update {
                    it.copy(
                        id = "",
                        date = DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(LocalDate.now()),
                        context = "",
                        done = false,
                        link = "",
                    )
                }
            }

            is ContactEvent.SetId ->{
                _state.update {
                    it.copy(
                        id = event.id
                    )
                }
            }

            is ContactEvent.SetDone ->{
                _state.update {
                    it.copy(
                        done = event.done
                    )
                }
            }


            is ContactEvent.SetContext -> {
                _state.update {
                    it.copy(
                        context = event.string
                    )
                }
            }

            is ContactEvent.SetDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is ContactEvent.SortContact -> {
                _sortType.value = event.sortType
            }

            is ContactEvent.SetLink ->{
                _state.update {
                    it.copy(
                        link = event.link
                    )
                }
            }

        }
    }
}
