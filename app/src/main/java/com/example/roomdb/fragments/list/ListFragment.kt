package com.example.roomdb.fragments.list

import android.app.AlertDialog
import android.app.Person
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.roomdb.R
import com.example.roomdb.viewmodel.UserViewModel
import com.example.roomdb.databinding.FragmentListBinding
import com.example.roomdb.model.User
import kotlinx.coroutines.launch

class ListFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener,
    SearchView.OnQueryTextListener {
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var _binding : FragmentListBinding
    private val binding get() = _binding
    private  val adapter: ListAdapter by lazy {
        ListAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
//        val adapter = ListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.floatingButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        setHasOptionsMenu(true)

        //Image TypeConverter
//        lifecycleScope.launch {
//            val person = User(0, "John", "Doe", getBitmap(), 25)
//            mUserViewModel.addUser(person)
//        }
        mUserViewModel.readAllData.observe(viewLifecycleOwner, Observer {user->
            adapter.setData(user)
        })
        return binding.root
    }

    private suspend fun getBitmap(): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data("https://avatars3.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4")
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        inflater.inflate(R.menu.search_menu, menu)

    val search = menu.findItem(R.id.menu_search)
    val searchView = search.actionView as SearchView
    searchView.isSubmitButtonEnabled = true
    searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteAllUsers()
        }
        return super.onContextItemSelected(item)
    }

    private fun deleteAllUsers() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            mUserViewModel.deleteAllUsers()
        }
        builder.setNegativeButton("No"){_,_ ->

        }
        builder.setTitle("Delete All Users")
        builder.setMessage("Do you really want to delete all users?")
        builder.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String){
        val searchQuery = "%$query%"
        mUserViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner){list->

            list?.let {
                adapter.setData(it)
            }

        }
    }
}
