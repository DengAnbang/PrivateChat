package com.abxh.jetpack.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

/**
 * Created by dab on 2021/8/10 14:54
 */
public class BaseViewModelActivity<VM : IViewModel> : AppCompatActivity() {
    lateinit var viewModel:VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IViewModel::class.java) as VM
    }
}