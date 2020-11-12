package rouchuan.viewpagerlayoutmanager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.leochuan.ScrollHelper;
import com.leochuan.ViewPagerLayoutManager;


/**
 * Created by Dajavu on 26/10/2017.
 */

public abstract class BaseActivity<V extends ViewPagerLayoutManager, S extends SettingPopUpWindow>
        extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView pageShow;
    private V viewPagerLayoutManager;
    protected S settingPopUpWindow;

    protected abstract V createLayoutManager();

    protected abstract S createSettingPopUpWindow();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setTitle(getIntent().getCharSequenceExtra(MainActivity.INTENT_TITLE));
        recyclerView = findViewById(R.id.recycler);
        pageShow = findViewById(R.id.page_show_tv);
        viewPagerLayoutManager = createLayoutManager();
        DataAdapter dataAdapter = new DataAdapter();
        dataAdapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Toast.makeText(v.getContext(), "clicked:" + pos, Toast.LENGTH_SHORT).show();
                ScrollHelper.smoothScrollToTargetView(recyclerView, v);
            }
        });
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(viewPagerLayoutManager);
        viewPagerLayoutManager.setOnPageChangeListener(new ViewPagerLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageShow.setText((position + 1) + "/" + dataAdapter.getItemCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Toast.makeText(this, "density: " + getResources().getDisplayMetrics().density, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        MenuItem settings = menu.findItem(R.id.setting);
        VectorDrawableCompat settingIcon =
                VectorDrawableCompat.create(getResources(), R.drawable.ic_settings_white_48px, null);
        settings.setIcon(settingIcon);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                showDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        if (settingPopUpWindow == null) {
            settingPopUpWindow = createSettingPopUpWindow();
        }
        settingPopUpWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
    }

    public V getViewPagerLayoutManager() {
        return viewPagerLayoutManager;
    }

    public S getSettingPopUpWindow() {
        return settingPopUpWindow;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingPopUpWindow != null && settingPopUpWindow.isShowing())
            settingPopUpWindow.dismiss();
    }
}
