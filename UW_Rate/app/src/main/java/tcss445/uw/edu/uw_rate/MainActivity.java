package tcss445.uw.edu.uw_rate;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.LoginFragmentInteractionListener,
        RegisterFragment.RegisterFragmentInteractionListener,
        SearchFragment.SearchFragmentInteractionListener,
        ProfessorFragment.ProfessorFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                new LoginFragment(),
                "Login")
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void loginFragmentInteraction(String theFragString, int theRadioCheck) {
        switch (theFragString) {
            case "RegisterFrag":
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.boo_key), theRadioCheck);
                Fragment frag = new RegisterFragment();
                frag.setArguments(bundle);
                switchFrag(frag, theFragString);
                break;
            case "SearchFrag":
                switchFrag(new SearchFragment(), theFragString);
                break;
        }
    }

    @Override
    public void registerFragmentInteraction(String theFragString) {
        switch (theFragString) {
            case "SearchFrag":
                switchFrag(new SearchFragment(), theFragString);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(String theFragString) {

    }

    @Override
    public void onProfessorSelected(Professor professor, String theFragString) {
        Bundle bundle = new Bundle();
        bundle.putString("professor", professor.getName());
        Fragment frag = new ProfessorFragment();
        frag.setArguments(bundle);
        switchFrag(frag, theFragString);
    }

    private void switchFrag(Fragment theFrag, String theFragString) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, theFrag, theFragString)
                .addToBackStack(theFragString);
        transaction.commit();
    }

    @Override
    public void professorFragmentInteraction(String theFragString, int theRadioCheck) {
        Log.d("professor", "inside professorfragmentinteraction");
    }
}
