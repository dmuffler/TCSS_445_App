package tcss445.uw.edu.uw_rate;

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