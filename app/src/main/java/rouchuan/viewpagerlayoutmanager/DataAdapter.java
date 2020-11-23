package rouchuan.viewpagerlayoutmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.leochuan.CarouselLayoutManager;
import com.leochuan.ViewPagerLayoutManager;

/**
 * Created by Dajavu on 25/10/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private CarouselLayoutManager viewPagerLayoutManager;

    private RecyclerView recyclerView;
    public int itemSpace = 0;
    public int maxVisibleItemCount = ViewPagerLayoutManager.DETERMINE_BY_MAX_AND_MIN;

    public void setViewPagerLayoutManager(CarouselLayoutManager viewPagerLayoutManager) {
        this.viewPagerLayoutManager = viewPagerLayoutManager;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public int[] images = {R.drawable.item0, R.drawable.item1, R.drawable.item2, R.drawable.item3,
            R.drawable.item4, R.drawable.item5, R.drawable.item6, R.drawable.item7,
            R.drawable.item8, R.drawable.item9, R.drawable.item10};

    public OnItemClickListener onItemClickListener;

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int totalWidth = recyclerView.getWidth();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        int width = getItemWidth();
        if(width != 0) {
            RecyclerView.LayoutParams params =  (RecyclerView.LayoutParams) view.getLayoutParams();
            params.width = width;
            view.setLayoutParams(params);
        }
        return new ViewHolder(view);
    }

    private int getItemWidth() {
        int width = 0;
        if(maxVisibleItemCount == ViewPagerLayoutManager.DETERMINE_BY_MAX_AND_MIN) {
            width = (int)(recyclerView.getMeasuredWidth() - itemSpace * 1.0f / 2);
        }else {
            width = recyclerView.getMeasuredWidth() - itemSpace * (maxVisibleItemCount - 1);
        }
        return width;
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageResource(images[position]);
        holder.imageView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       public ImageView imageView;
       public ImageView iconBgView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            iconBgView = itemView.findViewById(R.id.lock_bg);
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
