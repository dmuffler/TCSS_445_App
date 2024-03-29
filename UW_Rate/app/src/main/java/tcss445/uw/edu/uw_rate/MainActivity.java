package tcss445.uw.edu.uw_rate;

/*
 * UW Rate app which gives UW students the ability to rate professors at UW.
 */

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;

import tcss445.uw.edu.uw_rate.API.API;
import tcss445.uw.edu.uw_rate.API.CreateInstructorTask;
import tcss445.uw.edu.uw_rate.API.DeleteInstructorTask;

/**
 * @author Steven Smith
 * @author Donald Muffler
 * @author Dmitriy Bliznyuk
 * @author Brian Geving
 * Main activity deals with fragment transactions.
 */
public class MainActivity extends AppCompatActivity
        implements /*NavigationView.OnNavigationItemSelectedListener,*/
        LoginFragment.LoginFragmentInteractionListener,
        RegisterFragment.RegisterFragmentInteractionListener,
        SearchFragment.SearchFragmentInteractionListener,
        InstructorFragment.InstructorFragmentInteractionListener,
        SearchFragmentAdmin.SearchFragmentAdminInteractionListener,
        AddInstructorFragment.OnFragmentInteractionListener,
        InstructorFragmentAdmin.InstructorFragmentAdminInteractionListener,
        StudentSettingsFragment.StudentSettingsFragmentInteractionListener,
        AdminMenuFragment.AdminMenuFragmentInteractionListener,
        AdminStudentManagementFragment.AdminStudentManagementFragmentInteractionListener {

    private String sessionId;
    public static final String SESSION_PREFERENCES = "SESSION_PREFERENCES";
    public static final String SESSION = "SESSION";
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                    new LoginFragment(),
                    "Login")
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                toggleMenuItem(R.id.action_settings, false);
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mMenu = menu;
        toggleMenuItem(R.id.action_settings, false);
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
            switchFrag(new StudentSettingsFragment(), StudentSettingsFragment.class.getName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loginFragmentInteraction(String theFragString) {
        switch (theFragString) {
            case "RegisterFrag":
                Fragment frag = new RegisterFragment();
                switchFrag(frag, theFragString);
                break;
            case "SearchFrag":

                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                String is_admin = sharedPref.getString("is_admin", "null");
                
                if (Integer.parseInt(is_admin) == 1) {
                    switchFrag(new AdminMenuFragment(), AdminMenuFragment.class.getName());
                } else if (Integer.parseInt(is_admin) == 0) {
                    switchFrag(new SearchFragment(), theFragString);
                }
                toggleMenuItem(R.id.action_settings, true);
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
    public void onInstructorDelete(String instructorEmail) {
        new DeleteInstructorTask(getApplicationContext(), new API.Listener() {
            @Override
            public void onComplete(Object results) {
                Toast.makeText(getApplicationContext(), "Deleted instructor.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Failed to delete instructor.", Toast.LENGTH_SHORT).show();
            }
        }).delete(instructorEmail);
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

    @Override
    public void onCreateInstructor(Instructor instructor) {
        new CreateInstructorTask(getApplicationContext(), new API.Listener() {
            @Override
            public void onComplete(Object results) {
                Toast.makeText(getApplicationContext(), "Instructor created.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Failed to create instructor.", Toast.LENGTH_SHORT).show();
            }
        }).create(instructor);
    }

    private void toggleMenuItem(int theOption, boolean theSwitch) {
        mMenu.findItem(theOption).setEnabled(theSwitch);
    }

    @Override
    public void onAdminMenuItemSelected(Class<? extends Fragment> selectedFragment) {
        try {
            switchFrag(selectedFragment.newInstance(), selectedFragment.getName());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
