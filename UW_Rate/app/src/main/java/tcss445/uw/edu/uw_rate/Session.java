package tcss445.uw.edu.uw_rate;


import android.os.Parcel;
import android.os.Parcelable;

public class Session implements Parcelable {
    private final String sessionId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private boolean valid;

    public Session(String sessionId, String email, String firstName, String lastName) {
        this.sessionId = sessionId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.valid = true;
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public String getSessionId() {
        return sessionId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isValid() {
        return valid;
    }

    public void invalidate() {
        this.valid = false;
    }

    public Session(Parcel in) {
        this.sessionId = in.readString();
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        boolean[] tmp = new boolean[1];
        in.readBooleanArray(tmp);
        this.valid = tmp[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sessionId);
        parcel.writeString(email);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeBooleanArray(new boolean[] {valid});
    }
}
