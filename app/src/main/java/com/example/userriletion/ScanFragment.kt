package com.example.userriletion

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userriletion.databinding.FragmentScanBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ScanFragment : Fragment() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CODE = 42


    }

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var photoFile: File? = null
    private lateinit var ImageUri : Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takephoto.setOnClickListener{

        dispatchTakePictureIntent()
    }
    binding.uploadimage.setOnClickListener{

        uploadImage()
    }
    binding.selectimage.setOnClickListener{
        selectImage()
    }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)

    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    photoFile = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        context?.let { context ->
                            val photoURI: Uri = FileProvider.getUriForFile(
                                context,
                                "com.example.userriletion.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            Log.d("fotoBodoh", "Jalan ${data?.data!!}")
//
//                ImageUri = data.data!!
//            binding.image.setImageURI(ImageUri)
            val imageBitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)
            binding.image.setImageBitmap(imageBitmap)
            ImageUri=Uri.fromFile(File(photoFile?.absolutePath))

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }
    private fun uploadImage() {

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Uploading File ..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("Image/$fileName")
                storageReference.putFile(ImageUri).
                addOnSuccessListener {
                    binding.image.setImageURI(null)
                    Toast.makeText(requireActivity(), "Sukses Unggah Foto", Toast.LENGTH_SHORT)
                        .show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    val action = ScanFragmentDirections.actionScanFragmentToSolusiFragment(fileName)
                    findNavController().navigate(action)

                }.addOnFailureListener{
                    if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()
        }

    }
}