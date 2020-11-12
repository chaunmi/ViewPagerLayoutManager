package rouchuan.viewpagerlayoutmanager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.leochuan.ViewPagerLayoutManager;

/**
 * Created by Dajavu on 25/10/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private ViewPagerLayoutManager viewPagerLayoutManager;

    public void setViewPagerLayoutManager(ViewPagerLayoutManager viewPagerLayoutManager) {
        this.viewPagerLayoutManager = viewPagerLayoutManager;
    }

    private int[] images = {R.drawable.item0, R.drawable.item1, R.drawable.item2, R.drawable.item3,
            R.drawable.item4, R.drawable.item5, R.drawable.item6, R.drawable.item7,
            R.drawable.item8, R.drawable.item9, R.drawable.item10};

    public OnItemClickListener onItemClickListener;

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);
        holder.imageView.setTag(position);

        int width = holder.imageView.getWidth();
        if(viewPagerLayoutManager != null) {
            width = viewPagerLayoutManager.getItemWidth();
        }
        FrameLayout.LayoutParams params =  (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
        params.width = width;
        holder.imageView.setLayoutParams(params);

//        holder.imageView.post(new Runnable() {
//            @Override
//            public void run() {
//                int width = holder.imageView.getWidth();
//                if(viewPagerLayoutManager != null) {
//                    width = viewPagerLayoutManager.getItemWidth();
//                }
//                FrameLayout.LayoutParams params =  (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
//                params.width = width;
//                holder.imageView.setLayoutParams(params);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
