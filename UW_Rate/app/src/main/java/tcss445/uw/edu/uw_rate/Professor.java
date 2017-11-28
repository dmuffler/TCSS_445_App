package tcss445.uw.edu.uw_rate;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Professor implements Parcelable {

    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;

    public Professor(@NonNull String id, @NonNull String firstName, @NonNull String lastName,
                     @NonNull Gender gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    public Gender getGender() {
        return this.gender;
    }

    public Professor(Parcel in){
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.gender = Gender.fromChar((char) in.readByte());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeByte((byte) Gender.toChar(this.gender));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Professor createFromParcel(Parcel in) {
            return new Professor(in);
        }

        public Professor[] newArray(int size) {
            return new Professor[size];
        }
    };

    public static Professor fromInstructorResult(InstructorResult result) {
        return new Professor(result.instructor_id, result.first_name, result.last_name,
                Gender.fromChar(result.gender.toCharArray()[0]));
    }

    public enum Gender {
        MALE, FEMALE, OTHER;

        public static Gender fromChar(char key) {
            if (key == 'M' || key == 'm') {
                return Gender.MALE;
            } else if (key == 'F' || key == 'f') {
                return Gender.FEMALE;
            } else {
                return Gender.OTHER;
            }
        }

        public static char toChar(Gender gender) {
            if (gender.equals(Gender.MALE)) {
                return 'M';
            } else if(gender.equals(Gender.MALE)) {
                return 'F';
            } else {
                return '+';
            }
        }
    }
}
