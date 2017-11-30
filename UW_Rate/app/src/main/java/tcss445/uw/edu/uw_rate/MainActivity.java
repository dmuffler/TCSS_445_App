package tcss445.uw.edu.uw_rate;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.LoginFragmentInteractionListener,
        RegisterFragment.RegisterFragmentInteractionListener,
        SearchFragment.SearchFragmentInteractionListener,
        InstructorFragment.InstructorFragmentInteractionListener,
        SearchFragmentAdmin.SearchFragmentAdminInteractionListener,
        AddInstructorFragment.OnFragmentInteractionListener,
        InstructorFragmentAdmin.InstructorFragmentAdminInteractionListener {

    private String sessionId;
    public static final String SESSION_PREFERENCES = "SESSION_PREFERENCES";
    public static final String SESSION = "SESSION";

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
    public void loginFragmentInteraction(String theFragString) {
        switch (theFragString) {
            case "RegisterFrag":
                //Bundle bundle = new Bundle();
                Fragment frag = new RegisterFragment();
                //frag.setArguments(bundle);
                switchFrag(frag, theFragString);
                break;
            case "SearchFrag":

                //TODO need to check if admin here

                //switchFrag(new SearchFragment(), theFragString);
                switchFrag(new SearchFragmentAdmin(), theFragString);
                break;
        }
    }

    @Override
    public void registerFragmentInteraction(String theFragString) {
        switch (theFragString) {
            case "LoginFrag":
                getSupportFragmentManager().popBackStackImmediate();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(String theFragString) {

    }

    @Override
    public void onAddInstructor(String theFragString) {
        switch (theFragString) {
            case "AddInstructionFrag":
                Fragment frag = new AddInstructorFragment();
                switchFrag(frag, theFragString);
                break;
        }
    }

    @Override
    public void onInstructorSelectedAdmin(Instructor instructor, String theFragString) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Instructor.class.getName(), instructor);
        Fragment frag = new InstructorFragmentAdmin();
        frag.setArguments(bundle);
        switchFrag(frag, theFragString);
    }

    @Override
    public void onInstructorSelected(Instructor instructor, String theFragString) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Instructor.class.getName(), instructor);
        Fragment frag = new InstructorFragment();
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
    public void instructorFragmentInteraction(String theFragString) {
        Log.d("instructor", "inside instructorfragmentinteraction");
    }

    @Override
    public void onProfessorDelete(String theFragString) {

    }

    @Override
    public void onRatingChanged(Rating rating, API.Listener<RatingResult[]> listener) {
        API.APITask<RatingResult[]> task = new API.APITask<RatingResult[]>(this,
            listener,
            "rating",
            RatingResult[].class,
            "verb","instructor_email","score", "hotness", "comment");
        task.execute("CREATE_OR_UPDATE",
            rating.getInstructorId(),
            String.valueOf(rating.getScore()),
            String.valueOf(rating.getHotness()),
            rating.getComment());
    }

    @Override
    public void storeSession(Session session) {
        SharedPreferences preferences = getSharedPreferences(SESSION_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SESSION, new Gson().toJson(session));
        editor.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
