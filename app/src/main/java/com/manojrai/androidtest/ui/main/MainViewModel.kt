package com.manojrai.androidtest.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.manojrai.androidtest.data.local.DbHelper
import com.manojrai.androidtest.data.model.Book
import com.manojrai.androidtest.ui.base.BaseViewModel
import com.manojrai.androidtest.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val dbHelper: DbHelper
) : BaseViewModel(schedulerProvider, compositeDisposable) {

    val booksLiveData = MutableLiveData<ArrayList<Book>>()

    init {
        onCreate()
    }

    private fun onCreate() {
        if (!dbHelper.isExists()) {
            dbHelper.insertAuthor()
        }
        getBooks()
    }

    fun getBooks() {
        compositeDisposable.add(
            dbHelper.getBooks()
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    booksLiveData.postValue(it)
                   /* for (i in it) {
                        Log.d("Boook", " ${i.authorName}  ${i.list.size} ")
                        for (img in i.list) {
                            Log.d("Boook", "Img  ${img}")
                        }
                    }*/
                }, {
                    messageString.postValue(it.message)
                })
        )
    }
}