package com.manojrai.androidtest.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.manojrai.androidtest.utils.extensions.showToast

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    abstract val mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())
        setupObservers(savedInstanceState)
        setupView(savedInstanceState)

    }

    protected open fun setupObservers(savedInstanceState: Bundle?) {
        mViewModel.messageString.observe(this, Observer {
            it?.apply { showMessage(it) }
        })
    }

    fun showMessage(message: String) = showToast(message)

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun setupView(savedInstanceState: Bundle?)
}