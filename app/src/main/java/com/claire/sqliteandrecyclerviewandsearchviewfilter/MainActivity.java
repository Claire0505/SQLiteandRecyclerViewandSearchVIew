package com.claire.sqliteandrecyclerviewandsearchviewfilter;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.claire.sqliteandrecyclerviewandsearchviewfilter.adapter.MyRecyclerViewAdapter;
import com.claire.sqliteandrecyclerviewandsearchviewfilter.model.Item;
import com.claire.sqliteandrecyclerviewandsearchviewfilter.model.ItemDAO;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener{
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    private Item item = new Item();
    private ItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initHandler();

    }

    private void initHandler() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addNoteDialog();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        itemDAO = new ItemDAO(this);
        List<Item> allItems = itemDAO.getAll();

        if (allItems.size() > 0) {
            adapter = new MyRecyclerViewAdapter(this, allItems);
            recyclerView.setAdapter(adapter);
        } else {

            Snackbar.make(fab, "目前沒有資料", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void addNoteDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_item, null);
        final EditText title = subView.findViewById(R.id.editTitle);
        final EditText content = subView.findViewById(R.id.editContent);
        final TextView dataTime = subView.findViewById(R.id.add_dateTime);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("新增記事資料：");
        dialog.setView(subView);
        dialog.create();

        dialog.setPositiveButton("新增", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.setDatetime(new Date().getTime());
                dataTime.setText(item.getLocationDatetime());

                String titleStr = title.getText().toString();
                String contentStr = content.getText().toString();
                item.setTitle(titleStr);
                item.setContent(contentStr);
                itemDAO.insert(item);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initHandler();
                    }
                });
            }
        });
        dialog.setNegativeButton("取消", null);

        dialog.show();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu );

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Item> allItem = itemDAO.getAll();
        adapter = new MyRecyclerViewAdapter(this, allItem);
        adapter.filter(newText);
        recyclerView.setAdapter(adapter);

        return true;
    }
}
