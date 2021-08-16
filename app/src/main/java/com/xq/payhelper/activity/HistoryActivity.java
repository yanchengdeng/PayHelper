package com.xq.payhelper.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 显示App icon左侧的back键 */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_list);
        setTitle("历史订单");
        billListView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        ll_empty = findViewById(R.id.ll_empty);
        historyAdapter = new HistoryAdapter();
        billListView.setLayoutManager(new LinearLayoutManager(this));
        billListView.setAdapter(historyAdapter);
        setData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setData() {
        List<Bill> bills = HelperApplication.billDao().bills(size);
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
