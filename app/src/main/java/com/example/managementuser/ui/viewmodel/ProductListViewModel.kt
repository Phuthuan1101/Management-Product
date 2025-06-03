package com.example.managementuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.data.product.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProductListViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _pagedProducts = MutableLiveData<List<ProductEntity>>()
    val pagedProducts: LiveData<List<ProductEntity>> get() = _pagedProducts

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 0
    private val limit = 10
    private var hasMoreData = true

    init {
        loadCurrentPage()
    }

    private fun loadCurrentPage() {
        val currentList = _pagedProducts.value ?: emptyList()
        _pagedProducts.postValue(
            repository.getLocalPagedProducts(
                limit,
                page = currentPage
            ) + currentList
        )
    }

    fun loadMore() {
        if (_isLoading.value == true || !hasMoreData) return

        _isLoading.value = true
        val skip = currentPage * limit

        viewModelScope.launch {
            try {
                // Sau khi cập nhật DB xong, load dữ liệu local ra UI
                delay(1000)
                loadCurrentPage()
                currentPage++
                // Giả sử nếu API trả về ít hơn limit, là hết dữ liệu
                // Bạn cần sửa hàm fetchProducts để trả về số lượng bản ghi, hoặc thêm biến hasMoreData logic ở đây
                // Ví dụ: hasMoreData = (response.size >= limit)
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
                delay(1000)
                repository.deleteProductById(id)
            } catch (e: Exception) {
                _errorMessage.value = "Xóa product thất bại: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}