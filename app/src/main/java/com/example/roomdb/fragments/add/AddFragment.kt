package com.example.roomdb.fragments.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.roomdb.R
import com.example.roomdb.model.User
import com.example.roomdb.viewmodel.UserViewModel
import com.example.roomdb.databinding.FragmentAddBinding
import kotlinx.android.synthetic.main.custom_row.*
import kotlinx.coroutines.launch

class AddFragment : Fragment() {
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var _binding: FragmentAddBinding
    private val binding get() = _binding
    private var bitmap: Bitmap? = null
    val REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)



        binding.image.setOnClickListener {
            pickImage()
        }

        binding.addButton.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }


    fun pickImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, REQUEST_CODE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imageUri = data?.data // handle chosen image

            bitmap = MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                Uri.parse(imageUri.toString())
            )

            binding.image.setImageBitmap(bitmap)
        }


//        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun insertDataToDatabase() {
        val firstName = binding.firstNameEt.text.toString()
        val lastName = binding.lastNameEt.text.toString()
        val age = binding.ageEt.text
        if (inputCheck(firstName, lastName, age) && bitmap != null) {
            lifecycleScope.launch {
                val user = User(0, firstName, lastName, bitmap!!, Integer.parseInt(age.toString()))
                mUserViewModel.addUser(user)
            }
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all the fields!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }
}