package tcss445.uw.edu.uw_rate;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Instructor implements Parcelable {

    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;

    public Instructor(@NonNull String id, @NonNull String firstName, @NonNull String lastName,
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

    public Instructor(Parcel in){
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
        public Instructor createFromParcel(Parcel in) {
            return new Instructor(in);
        }

        public Instructor[] newArray(int size) {
            return new Instructor[size];
        }
    };

    public static Instructor fromInstructorResult(InstructorResult result) {
        return new Instructor(result.instructor_id, result.first_name, result.last_name,
                Gender.fromChar(result.gender.toCharArray()[0]));
    }
}
