package tcss445.uw.edu.uw_rate;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Instructor implements Parcelable {

    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String positionTitle;
    private String departmentName;

    public Instructor(@NonNull String email, String phoneNumber,
                      String firstName, String lastName,
                      String positionTitle, String departmentName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.positionTitle = positionTitle;
        this.departmentName = departmentName;
    }

    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() { return phoneNumber;}
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getPositionTitle() { return this.positionTitle; }
    public String getDepartmentName() { return this.departmentName; }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    public Instructor(Parcel in){
        this.email = in.readString();
        this.phoneNumber = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.positionTitle = in.readString();
        this.departmentName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.positionTitle);
        dest.writeString(this.departmentName);
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
        return new Instructor(result.email, result.phone_number,
                result.first_name, result.last_name,
                result.position_title, result.department_name);
    }
}
