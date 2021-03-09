package ru.firstset.whereisuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_MAP = "FRAGMENT_MAP";
    private static final String FRAGMENT_HISTORY = "FRAGMENT_HISTORY";

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutContainer, new MyMapFragment(), FRAGMENT_MAP)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMap: {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutContainer, new MyMapFragment(), FRAGMENT_MAP)
                        .commit();
                break;
            }
            case R.id.menuHistory: {

                fragmentManager = getSupportFragmentManager();
                fragmentManager.findFragmentByTag(FRAGMENT_HISTORY);
                fragmentManager.beginTransaction()
                        .addToBackStack(FRAGMENT_HISTORY)
                        .replace(R.id.frameLayoutContainer, new FragmentHistory(), FRAGMENT_HISTORY)
                        .commit();
                break;
            }
            case R.id.menuExit:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}