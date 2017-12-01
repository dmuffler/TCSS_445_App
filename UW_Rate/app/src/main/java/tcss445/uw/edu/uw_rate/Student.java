package tcss445.uw.edu.uw_rate;

class Student {

    private String firstName;
    private String lastName;
    private String email;

    public Student(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
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

    public static Student[] fromResultArray(StudentResult[] results) {
        Student[] students = new Student[results.length];
        StudentResult ptr;
        for (int i = 0; i < results.length; i++) {
            ptr = results[i];
            students[i] = new Student(ptr.email, ptr.first_name, ptr.last_name);
        }
        return students;
    }
}
