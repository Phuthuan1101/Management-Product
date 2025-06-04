package com.example.managementuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.data.product.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _pagedProducts = MutableStateFlow<List<ProductEntity>>(emptyList())
    val pagedProducts: StateFlow<List<ProductEntity>> get() = _pagedProducts.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPage = 0
    private val limit = 10
    private var hasMoreData = true

    val productsBestSale: List<ProductEntity> = repository.getProductsBestSales()

    init {
        loadFirstPage()
    }

    private fun loadFirstPage() {
        _pagedProducts.value = emptyList()
        currentPage = 0
        hasMoreData = true
        loadMore()
    }

    fun loadMore() {
        if (_isLoading.value || !hasMoreData) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val newProducts = repository.getLocalPagedProducts(limit, page = currentPage)
                val currentList = _pagedProducts.value
                if (newProducts.size < limit) {
                    hasMoreData = false
                }
                // Đảm bảo không add trùng sản phẩm
                val updatedList = currentList + newProducts.filterNot { p -> currentList.any { it.id == p.id } }
                _pagedProducts.value = updatedList
                if (newProducts.isNotEmpty()) {
                    currentPage++
                }
                delay(1000)
            } catch (e: Exception) {
                _errorMessage.value = "Lấy dữ liệu thất bại: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteProductById(id)
                // Xóa khỏi list hiện tại
                _pagedProducts.value = _pagedProducts.value.filterNot { it.id == id }
            } catch (e: Exception) {
                _errorMessage.value = "Xóa product thất bại: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadFirstPage()
    }
}