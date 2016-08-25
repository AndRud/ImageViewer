package com.andrutyk.imageviewer.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andrutyk.imageviewer.R;
import com.andrutyk.imageviewer.image_fragment.ImagePagerFragment;
import com.andrutyk.imageviewer.preference.ImagePreferenceActivity;
import com.andrutyk.imageviewer.preference.ImagePreferenceFragment;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private final static String FRAGMENT_TAG = "main_fragment";

    private Fragment fragmentMain;

    private String[] categories;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDrawerLayout();
        addFragment();
    }

    private void initDrawerLayout(){
        categories = getResources().getStringArray(R.array.categories_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, categories));
        // Set the list's click listener
        drawerList.setOnItemClickListener(this);
    }

    private void addFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentMain = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragmentMain == null){
            fragmentMain = new ImagePagerFragment();
            fragmentTransaction.add(R.id.fragmentContent, fragmentMain, FRAGMENT_TAG);
        } else {
            fragmentTransaction.replace(R.id.fragmentContent, fragmentMain);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ImagePreferenceActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.ivImage) {
            getMenuInflater().inflate(R.menu.image_context_menu, menu);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (fragmentMain != null) {
            ((ImagePagerFragment)fragmentMain).setCategory(position);
        }
        selectDrawerItem(position);
    }

    private void selectDrawerItem(int position) {
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
    }
}