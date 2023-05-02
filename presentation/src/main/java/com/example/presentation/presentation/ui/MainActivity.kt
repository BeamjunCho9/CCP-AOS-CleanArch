package com.example.presentation.presentation.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.domain.model.Categories
import com.example.presentation.R
import com.example.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var categoryPos : String? = null
    private lateinit var cateList : Categories
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 카테고리 데이터 받아오기 성공
        viewModel.categories.observe(this) {
            var adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, it)
            binding.categorySpinner.adapter = adapter

            cateList = it
            Log.d("로그(액티비티)", cateList.toString())

//          배경 투명도 조절
            binding.mainLayout.setBackgroundColor(Color.parseColor("#5644e6"));
//          ProgressBar 숨기기
            binding.loadingStatus.visibility= View.GONE
//          터치 방지 해제
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        // 농담 불러오기 성공
        viewModel.jokes.observe(this) {
            binding.jokeContent.text = it.value
        }

    }

    override fun onResume() {
        super.onResume()

//        카테고리 불러오기
        GlobalScope.launch {
            viewModel.loadCategories()
        }

        // 농담 불러오는 버튼 클릭 시
        binding.printBtn.setOnClickListener {
            GlobalScope.launch {
                Log.d("버튼 클릭", categoryPos.toString())
                categoryPos?.let { query ->


                    if (categoryPos == "random") {
                        viewModel.loadJokes(null)
                    }
                    else
                        viewModel.loadJokes(query)
                }
            }
        }

        binding.categorySpinner.setSelection(0)
//      스피너의 아이템이 클릭 되었을 때 동작
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                선택된 카테고리를 String으로 받아옴
                categoryPos = cateList[p2]

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }
}