package com.example.roomdb.fragments.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roomdb.Constant
import com.example.roomdb.R
import com.example.roomdb.databinding.FragmentUpdateBinding
import com.example.roomdb.model.User
import com.example.roomdb.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class UpdateFragment : Fragment() {
    private lateinit var _binding : FragmentUpdateBinding
    private val binding get() = _binding
    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel
    private var bitmap : Bitmap?=null
    val REQUEST_CODE = 100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentUpdateBinding.inflate(inflater, container, false)
        binding.updateFirstNameEt.setText(args.currentUser.firstName)
        binding.updateLastNameEt.setText(args.currentUser.lastName)
        binding.updateAgeEt.setText(args.currentUser.age.toString())
        binding.imageView.setImageBitmap(args.currentUser.profilePhoto)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.updateButton.setOnClickListener {
            updateItem()
        }
        binding.imageView.setOnClickListener{
            pickImage()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun pickImage() {
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

            binding.imageView.setImageBitmap(bitmap)
        }


//        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun updateItem() {
        val id = args.currentUser.id
        val firstName = binding.updateFirstNameEt.text.toString()
        val lastName = binding.updateLastNameEt.text.toString()
        val age = binding.updateAgeEt.text
        if (inputCheck(firstName, lastName, age) && bitmap!=null){
            lifecycleScope.launch {
                val updatedUser = User(id,firstName,lastName, bitmap!!,Integer.parseInt(age.toString()))
                mUserViewModel.updateUser(updatedUser)
            }
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "User updated successfully!", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(requireContext(), "Please fill out all the fields!", Toast.LENGTH_SHORT).show()
        }

    }
    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteUser()
        }
        return super.onContextItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            mUserViewModel.deleteUser(args.currentUser)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "${args.currentUser.firstName} was deleted successfully!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Delete ${args.currentUser.firstName}?")
        builder.setMessage("Do you really want to delete ${args.currentUser.firstName}?")
        builder.create().show()
    }
}
