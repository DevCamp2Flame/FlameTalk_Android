package com.sgs.devcamp2.flametalk_android.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSearchBinding
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/02/11
 * @updated 2022/02/11
 * @desc 친구 검색. 친구 목록의 유저를 검색할 수 있다.
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val viewModel: SearchViewModel by viewModels()

    // 뷰 생성 시점에 adapter 초기화
    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUI()
        return binding.root
    }

    private fun initUI() {
        initSearch()
        initSearchData()

        binding.imgSearchBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initSearch() {
        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            Timber.d("입력된 텍스트 $text")
            viewModel.searchFriend(text.toString())
        }
    }

    // 검색 리스트 초기화
    private fun initSearchData() {
        binding.rvSearchFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchFriend.adapter = searchAdapter

        lifecycleScope.launch {
            viewModel.searchedFriend.collectLatest {
                if (it.isNullOrEmpty()) {
                    binding.cstSearchFriend.toVisibleGone()
                    binding.tvSearchFriendEmpty.toVisible()
                } else {
                    searchAdapter.data = it
                    searchAdapter.notifyDataSetChanged()
                    binding.cstSearchFriend.toVisible()
                    binding.tvSearchFriendEmpty.toVisibleGone()
                }
            }
        }
    }

    companion object {
        const val USER_DEFAULT_PROFILE = 1
        const val BIRTHDAY_FRIEND = 100
        const val FRIEND = 200

        const val GONE = 8
        const val VISIBLE = 0
    }
}
