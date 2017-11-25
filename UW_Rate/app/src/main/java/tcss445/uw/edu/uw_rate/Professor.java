package tcss445.uw.edu.uw_rate;


import android.support.annotation.NonNull;

public class Professor {

    private String name;

    public Professor(@NonNull String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
