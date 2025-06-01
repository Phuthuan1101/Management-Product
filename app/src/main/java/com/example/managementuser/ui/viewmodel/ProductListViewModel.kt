package com.example.managementuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.data.product.ProductRepository
import kotlinx.coroutines.launch


class ProductListViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _pagedProducts = MutableLiveData<LiveData<List<ProductEntity>>>()
    val pagedProducts: LiveData<List<ProductEntity>> get() = _pagedProducts.switchMap { it }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 0
    private val limit = 6
    private var hasMoreData = true

    init {
        loadCurrentPage()
    }

    private fun loadCurrentPage() {
        _pagedProducts.value = repository.getLocalPagedProducts(limit, currentPage)
    }

    fun loadMore() {
        if (_isLoading.value == true || !hasMoreData) return

        _isLoading.value = true
        val skip = currentPage * limit

        viewModelScope.launch {
            try {
                // Lấy dữ liệu từ API và lưu vào DB
                repository.fetchAndSaveProducts(
                    limit,
                    skip
                )  // giả sử đây là hàm fetch từ API rồi lưu DB

                // Sau khi cập nhật DB xong, load dữ liệu local ra UI
                loadCurrentPage()

                currentPage++
                // Giả sử nếu API trả về ít hơn limit, là hết dữ liệu
                // Bạn cần sửa hàm fetchProducts để trả về số lượng bản ghi, hoặc thêm biến hasMoreData logic ở đây
                // Ví dụ: hasMoreData = (response.size >= limit)
            } catch (e: Exception) {
                _errorMessage.value = "Lấy dữ liệu thất bại: ${e.message}"
                loadCurrentPage()
                currentPage++
            } finally {
                _isLoading.value = false
            }
        }
    }

    //Custom switchMap helper
    fun <X, Y> LiveData<X>.switchMap(transform: (X) -> LiveData<Y>): LiveData<Y> {
        val result = MediatorLiveData<Y>()
        var source: LiveData<Y>? = null

        result.addSource(this) { x ->
            val newLiveData = transform(x)
            if (source === newLiveData) {
                return@addSource
            }
            source?.let { result.removeSource(it) }
            source = newLiveData
            result.addSource(newLiveData) { y ->
                result.value = y
            }
        }
        return result
    }
}