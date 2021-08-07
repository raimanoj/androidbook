package com.manojrai.androidtest.ui.addnew

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.manojrai.androidtest.R
import com.manojrai.androidtest.data.model.Book
import com.manojrai.androidtest.utils.common.DateUtils
import com.manojrai.androidtest.utils.extensions.showToast
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.fragment_add_new.*
import org.koin.android.ext.android.inject


class AddNewFragment : Fragment() {

    private val viewModel: AddNewViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var callBack: (Boolean) -> Unit

    private var authorName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new, container, false)
    }

    fun setCallBack(callBack: (Boolean) -> Unit) {
        this.callBack = callBack
    }

    companion object {

        const val TAG = "AddNewFragment"

        fun newInstances(): AddNewFragment {
            val args = Bundle()
            val fragment = AddNewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onCreate()
        edtDOI.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
            picker.addOnPositiveButtonClickListener {
                edtDOI.setText(DateUtils.getDate(it))
            }
        }

        btnCoverPhoto.setOnClickListener {
            selectImages()
        }
        btnSubmit.setOnClickListener {
            if (edtBookName.text.toString().trim().isEmpty()) {
                edtBookName.error = "Enter Book Name"
                return@setOnClickListener
            }
            if (edtPrice.text.toString().trim().isEmpty()) {
                edtPrice.error = "Enter Price"
                return@setOnClickListener
            }
            if (edtDOI.text.toString().trim().isEmpty()) {
                edtDOI.error = "Select DOI"
                return@setOnClickListener
            }
            viewModel.onSubmit(
                Book(
                    null, edtBookName.text.toString(),
                    authorName, edtPrice.text.toString(), edtDOI.text.toString()
                )
            )
        }

        rvShowImage.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.authorLivData.observe(viewLifecycleOwner, {
            val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = aa
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    authorName = it[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        })

        viewModel.dataList.observe(viewLifecycleOwner, {
            val imgAdapter = ShowImageAdapter(it)
            rvShowImage.adapter = imgAdapter
        })

        viewModel.submittedLiveData.observe(viewLifecycleOwner, {
            requireActivity().supportFragmentManager.popBackStack()
            callBack(it)
        })
        viewModel.messageString.observe(viewLifecycleOwner,{
            showToast(it)
        })
    }

    private fun selectImages() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        FilePickerBuilder.instance
                            .setActivityTheme(R.style.LibAppTheme) //optional
                            .pickPhoto(this@AddNewFragment)
                    }
                    if (p0.isAnyPermissionPermanentlyDenied) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setCancelable(false)
                            .setTitle("Camera & Storage Permissions Required!")
                            .setMessage("App needs camera and storage permissions to capture and select photos!")
                            .setPositiveButton("OKAY") { dialog, which ->
                                dialog.dismiss()
                                // openSettings
                            }.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }
            }).onSameThread().check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val uris =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                        ?: return
                viewModel.showUris(uris)
            }
        }
    }
}