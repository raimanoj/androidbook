package com.manojrai.androidtest.ui.addnew

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.manojrai.androidtest.data.local.DbHelper
import com.manojrai.androidtest.data.model.Book
import com.manojrai.androidtest.data.model.ImageModel
import com.manojrai.androidtest.ui.base.BaseViewModel
import com.manojrai.androidtest.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class AddNewViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val dbHelper: DbHelper
) : BaseViewModel(schedulerProvider, compositeDisposable) {


    val authorLivData = MutableLiveData<ArrayList<String>>()
    val listOfImage = ArrayList<ImageModel>()
    val dataList = MutableLiveData<ArrayList<ImageModel>>()
    val submittedLiveData = MutableLiveData<Boolean>()

    fun onCreate() {
        authorLivData.postValue(dbHelper.getAuthor())
    }

    fun showUris(
        list: ArrayList<Uri>
    ) {
        compositeDisposable.add(
            Single.fromCallable {
                val imageList = ArrayList<ImageModel>()
                for (uri in list) {
                    imageList.add(ImageModel(uri))
                    listOfImage.add(ImageModel(uri))
                }
                return@fromCallable imageList
            }.subscribeOn(schedulerProvider.io())
                .subscribe({
                    dataList.postValue(it)
                }, {
                    messageString.postValue(it.message)
                })
        )
    }

    fun onSubmit(book: Book) {
        if (listOfImage.isEmpty()) {
            messageString.postValue("Select image")
            return
        }
        compositeDisposable.add(
            dbHelper.insertData(book)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    for (img in listOfImage) {
                        dbHelper.insertImageUri(it, img.uri.toString())
                    }
                    submittedLiveData.postValue(true)
                }, {
                    messageString.postValue(it.message)
                })

        )
    }

}