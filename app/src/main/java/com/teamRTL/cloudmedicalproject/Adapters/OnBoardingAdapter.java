package com.teamRTL.cloudmedicalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.google.protobuf.Any;
import com.teamRTL.cloudmedicalproject.Models.OnBoadringItem;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;

public class OnBoardingAdapter extends PagerAdapter {
    Context bContext;
    List<OnBoadringItem> bBoardingList;

    public OnBoardingAdapter(Context bContext, List<OnBoadringItem> bBoardingList) {
        this.bContext = bContext;
        this.bBoardingList = bBoardingList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) bContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.on_boarding_screen,null);
        ImageView imageSlide = layoutScreen.findViewById(R.id.screenImage);
        TextView title = layoutScreen.findViewById(R.id.title);
        TextView description = layoutScreen.findViewById(R.id.description);

        title.setText(bBoardingList.get(position).getTitle());
        description.setText(bBoardingList.get(position).getDescription());
        imageSlide.setImageResource(bBoardingList.get(position).getScreenImage());

        container.addView(layoutScreen);
        return layoutScreen;

    }

    @Override
    public int getCount() {
        return bBoardingList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
