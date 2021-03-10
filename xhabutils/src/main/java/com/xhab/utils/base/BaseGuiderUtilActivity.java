package com.xhab.utils.base;


import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.xhab.utils.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by dab on 2021/3/8 12:01
 */
public abstract class BaseGuiderUtilActivity extends BaseUtilActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_just_guide;
    }

    public abstract List<Integer> setImages();

    public abstract void next();

    @Override
    public void initView() {
        super.initView();
        ViewPager viewPager = findViewById(R.id.viewpager);
        GuideAdapter guideAdapter = new GuideAdapter(setImages());
        viewPager.setAdapter(guideAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        RadioButton radioButton1 = findViewById(R.id.radiobutton1);
                        radioButton1.setChecked(true);
                        break;
                    case 1:
                        RadioButton radioButton2 = findViewById(R.id.radiobutton2);
                        radioButton2.setChecked(true);
                        break;
                    case 2:
                        RadioButton radioButton3 = findViewById(R.id.radiobutton3);
                        radioButton3.setChecked(true);
                        break;
                    case 3:
                        RadioButton radioButton4 = findViewById(R.id.radiobutton4);
                        radioButton4.setChecked(true);
                        break;
                }
                visibility(R.id.btn_skip, position != setImages().size() - 1);
                visibility(R.id.tv_finish, position == setImages().size() - 1);
                visibility(R.id.rg_indicate, position != setImages().size() - 1);
            }
        });
        click(R.id.btn_skip, view -> {
            next();
        });
        click(R.id.tv_finish, view -> {
            next();
        });

    }

    public class GuideAdapter extends PagerAdapter {
        private List<Integer> mIntegers;

        public GuideAdapter(List<Integer> integers) {
            mIntegers = integers;
        }

        @Override
        public int getCount() {
            return mIntegers.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            RelativeLayout relativeLayout = new RelativeLayout(container.getContext());
            Integer res = mIntegers.get(position);
            ImageView photoView = new ImageView(container.getContext());

            photoView.setBackgroundColor(Color.WHITE);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            photoView.setImageResource(res);
            relativeLayout.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(relativeLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            return relativeLayout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
