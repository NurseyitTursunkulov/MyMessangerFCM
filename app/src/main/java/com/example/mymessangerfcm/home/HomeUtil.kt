package com.example.mymessangerfcm.home

import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.mymessangerfcm.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*

fun HomeFragment.setUpViewPagerWithTabLayout() {
    lateinit var viewPager: ViewPager2
    val activity: AppCompatActivity = activity as AppCompatActivity
    val fragmentView =
        requireNotNull(view) { "View should not be null when calling onActivityCreated" }

    val toolbar: Toolbar = toolbar
    activity.setSupportActionBar(toolbar)

    val tabLayout: TabLayout = tabs
    val chatTab: TabLayout.Tab = tabLayout.getTabAt(2)!!
    viewPager = fragmentView.findViewById(R.id.view_pager)
    viewPager.adapter = TabsAdapter(childFragmentManager, lifecycle)

    // connect the tabs and view pager2
    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        tab.text = TAB_TITLES[position]
        if (position == 0) {
            tab.setIcon(R.drawable.ic_camera_alt_black_24dp)
        }
        viewPager.setCurrentItem(tab.position, true)
    }.attach()
    tabLayout.selectTab(chatTab)

    tabLayout.setTabWidthAsWrapContent(0)

}

fun TabLayout.setTabWidthAsWrapContent(tabPosition: Int) {
    val layout = (this.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
    val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
    layoutParams.weight = 0f
    layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
    layout.layoutParams = layoutParams
}
