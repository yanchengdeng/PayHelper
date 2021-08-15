package com.xq.payhelper.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xq.payhelper.R;
import com.xq.payhelper.HelperApplication;
import com.xq.payhelper.HistoryAdapter;
import com.xq.payhelper.entity.Bill;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView billListView;
    private HistoryAdapter historyAdapter;
    private LinearLayout ll_empty;
    private int size = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 显示App icon左侧的back键 */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_list);
        setTitle("历史订单");
        billListView = findViewById(R.id.recyclerView);
        ll_empty = findViewById(R.id.ll_empty);
        historyAdapter = new HistoryAdapter();
        billListView.setLayoutManager(new LinearLayoutManager(this));
        billListView.setAdapter(historyAdapter);
        List<Bill> bills = HelperApplication.billDao().bills( size);
        if (bills != null && bills.size() > 0) {
            ll_empty.setVisibility(View.GONE);
            historyAdapter.refreshBills(bills);
        } else {
            ll_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
