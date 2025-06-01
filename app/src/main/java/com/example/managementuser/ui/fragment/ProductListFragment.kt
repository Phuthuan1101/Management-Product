package com.example.managementuser.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managementuser.R
import com.example.managementuser.ui.adapter.ProductAdapter
import com.example.managementuser.ui.viewmodel.ProductListViewModel

class ProductListFragment : Fragment() {

    private lateinit var viewModel: ProductListViewModel
    private lateinit var adapter: ProductAdapter

    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var progressBar: View

    private var currentPage = 1
    private val pageSize = 10


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list_products_view, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ProductAdapter(emptyList()){ product, isChecked ->
            if (isChecked) {
                Log.d("Selected", "Selected product: ${product.title}")
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Lấy ViewModel từ Activity để dùng chung instance
        viewModel = ViewModelProvider(requireActivity())[ProductListViewModel::class.java]

        viewModel.pagedProducts.observe(viewLifecycleOwner) { products ->
            adapter.setProducts(products)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        return view
    }
}
