package com.example.portableanti_theft.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.portableanti_theft.domain.LocationSet;
import com.example.portableanti_theft.R;

import java.util.List;

public class LocationSetAdapter extends RecyclerView.Adapter<LocationSetAdapter.ViewHolder> {

    private List<LocationSet> mLocationSetList;
    private int resourceId;

    //定义一个内部类ViewHolder,该类继承自RecyclerView.ViewHolder。
    //需要传入一个View参数，通常是RecyclerView子项最外层布局
    static class ViewHolder extends RecyclerView.ViewHolder {
        //        ImageView provinceImage;

        TextView locationsetName;

        public ViewHolder(View view) {
            super(view);
            locationsetName = (TextView) view.findViewById(R.id.location_set_name);
        }
    }

    //    该构造函数作用是将数据源赋值给一个全局变量mProvinceList
    public LocationSetAdapter(List<LocationSet> locationSetList) {
        mLocationSetList = locationSetList;
    }

//  由于LocationSetAdapter是继承自RecyclerView.Adapter，
//  所以需要重写三个方法：
// onCreateViewHolder() ：创建ViewHolder实例，将局部加载进来
//  onBindViewHolder() ：对RecyclerView子项数据进行赋值
//  getItemCount()

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_set_item,
                viewGroup,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position
    ) {
        LocationSet locationSet = mLocationSetList.get(position);
        holder.locationsetName.setText(locationSet.getLocationname());
    }

    @Override
    public int getItemCount() {
        return mLocationSetList.size();
    }
}
