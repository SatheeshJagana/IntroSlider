package com.example.personal.introslider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    IntroManager introManager;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLinearLayout;
    private TextView[] dots_text;
    private int[] layouts;
    private Button skip_btn, next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //checking for the firsttime launch on app before calling setContentVIew

        introManager = new IntroManager(this);
        if (!introManager.isFirstTimeLaunch())
        {
            launchHomeScreen();
            finish();
        }

        //Making Notification bar transparent

        if(Build.VERSION.SDK_INT >= 21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        skip_btn = (Button) findViewById(R.id.btn_skip);
        next_btn = (Button) findViewById(R.id.btn_next);
        dotsLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_dots);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //all layouts here

        layouts = new int[] {R.layout.activity_screen1, R.layout.activity_screen2, R.layout.activity_screen3, R.layout.activity_screen4,
                            R.layout.activity_screen5};

        //adding dots in the bottom of the page

        addBottonDots(0);

        //Making the Notification bar color transparent

        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        //skip button to go home screen

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchHomeScreen();
            }
        });

        //next button to proceed for launch home screen or proceed for other screens

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //Checking for last page, if last page home screen will be launched

                int currentPage = getItem(+1);
                if(currentPage < layouts.length)
                {
                    //move to next screen
                    viewPager.setCurrentItem(currentPage);
                }
                else
                {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottonDots(int current)
    {
        dots_text = new TextView[layouts.length];
        int[] colors_active = getResources().getIntArray(R.array.dot_active);
        int[] colors_inactive = getResources().getIntArray(R.array.dot_inactive);

        dotsLinearLayout.removeAllViews();
        for (int i=0; i<dots_text.length;i++)
        {
            dots_text[i] = new TextView(this);
            dots_text[i].setText(Html.fromHtml("&#8226;"));
            dots_text[i].setTextSize(30);
            dots_text[i].setTextColor(colors_inactive[current]);
            dotsLinearLayout.addView(dots_text[i]);
        }

        if (dots_text.length > 0)
        {
            dots_text[current].setTextColor(colors_active[current]);
        }
    }

    private int getItem(int i)
    {
        return viewPager.getCurrentItem() +i;
    }

    private void launchHomeScreen()
    {
        introManager.setForFirstTimeLaunch(false);
        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottonDots(position);

            //changing the next button text to got it
            if (position == layouts.length -1)
            {
                //lastpage make button text to got it
                next_btn.setText("GOT IT");
                skip_btn.setVisibility(View.GONE);
            }
            else
            {
                //still pages left
                next_btn.setText("NEXT");
                skip_btn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void changeStatusBarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    public class MyViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter()
        {
            //empty constructor
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}

