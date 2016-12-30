# TabLayout

Usage:

        In your Layout file:
        
        <com.example.lyons.demo.customerview.TabLayout
                android:id="@+id/tl_test"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />
        
        In your Activity Or Fragment:
        
        CharSequence[] titles = new CharSequence[mFragments.size()];
        for (int i = 0; i < mFragments.size(); i++) {
            titles[i] = "tab" + i;
        }
        tab.setTabs(titles);
        tab.setViewPager(mViewPager);
