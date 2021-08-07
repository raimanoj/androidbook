package com.manojrai.androidtest.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.manojrai.androidtest.R
import com.manojrai.androidtest.data.model.Book
import com.manojrai.androidtest.ui.addnew.AddNewFragment
import com.manojrai.androidtest.ui.base.BaseActivity
import com.manojrai.androidtest.utils.extensions.showToast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<MainViewModel>() {

    override val mViewModel: MainViewModel by inject()

    override fun provideLayoutId() = R.layout.activity_main
    private val bookAdapter = BookAdapter(ArrayList()) {
        showSlider(it)
    }

    override fun setupView(savedInstanceState: Bundle?) {

        fabAddNew.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = AddNewFragment.newInstances()
            fragmentTransaction.add(R.id.frameContainer, fragment, AddNewFragment.TAG)
            fragmentTransaction.addToBackStack(AddNewFragment.TAG)
            fragment.setCallBack {
                fabAddNew.show()
                rvBooks.visibility = View.VISIBLE
                frameContainer.visibility = View.GONE
                mViewModel.getBooks()
            }
            fragmentTransaction.commit()
            rvBooks.visibility = View.GONE
            frameContainer.visibility = View.VISIBLE
            fabAddNew.hide()

        }

        rvBooks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }
        mViewModel.booksLiveData.observe(this, {
            bookAdapter.addList(it)
        })

    }

    private fun showSlider(book: Book) {
        val dialogView = layoutInflater.inflate(R.layout.slider_dialog, null)
        val dialog: AlertDialog =
            MaterialAlertDialogBuilder(this)
                .setTitle("Images")
                .setView(dialogView).show()
        dialog.setCanceledOnTouchOutside(false)
        val rvSlider = dialogView.findViewById<RecyclerView>(R.id.rvSlider)
        rvSlider.apply {
            layoutManager =
                LinearLayoutManager(rvSlider.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = SliderAdapter(book.list)
        }
    }

}