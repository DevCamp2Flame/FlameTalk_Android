package com.sgs.devcamp2.flametalk_android.ui.friend.birthday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentBirthdayFriendBinding
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author 박소연
 * @created 2022/02/18
 * @desc 생인친 친구 리스트
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class BirthdayFragment : Fragment() {
    private val binding by lazy { FragmentBirthdayFriendBinding.inflate(layoutInflater) }
    private val viewModel: BirthdayViewModel by viewModels()

    // 뷰 생성 시점에 adapter 초기화
    private val birthdayAdapter: BirthdayAdapter by lazy {
        BirthdayAdapter(requireContext())
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
        initAppbar()
        initBirthdayList()
        initDate()
    }

    private fun initAppbar() {
        binding.abBirthdayFriend.tvAppbar.text = "생일인 친구"
        binding.abBirthdayFriend.imgAppbarBack.toVisible()
        binding.abBirthdayFriend.imgAppbarSearch.toVisibleGone()
        binding.abBirthdayFriend.imgAppbarAddFriend.toVisibleGone()
        binding.abBirthdayFriend.imgAppbarSetting.toVisibleGone()

        binding.abBirthdayFriend.imgAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    //  생일인 친구 리스트 초기화
    private fun initBirthdayList() {
        binding.rvBirthdayFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBirthdayFriend.adapter = birthdayAdapter

        lifecycleScope.launch {
            viewModel.birthdayFriend.collectLatest {
                when {
                    it == null -> {
                        binding.rvBirthdayFriend.toVisible()
                        binding.tvBirthdayFriendEmpty.toVisibleGone()
                    }
                    it.isEmpty() -> { // is empty
                        showEmptyView(VISIBLE)
                    }
                    else -> {
                        birthdayAdapter.data = it
                        birthdayAdapter.submitList(it)
                        showEmptyView(GONE)
                    }
                }
            }
        }
    }

    private fun showEmptyView(type: Int) {
        binding.rvBirthdayFriend.visibility = 8 - type
        binding.tvBirthdayFriendEmpty.visibility = type
    }

    private fun initDate() {
        val now: Long = System.currentTimeMillis()
        val mDate = Date(now)
        val simpleDate = SimpleDateFormat("M월 d일")
        val getTime: String = simpleDate.format(mDate)
        binding.tvBirthdayFriendDate.text = getTime
    }

    companion object {
        const val GONE = 8
        const val VISIBLE = 0
    }
}
