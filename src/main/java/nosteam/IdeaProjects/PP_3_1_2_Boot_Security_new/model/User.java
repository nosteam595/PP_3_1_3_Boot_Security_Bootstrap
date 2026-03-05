package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 15, message = "Имя должно быть от 2 до 15 символов")
    @Column(name = "name")
    private String firstName;

    @NotEmpty(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 15, message = "Фамилия должна быть от 2 до 15 символов")
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 2, max = 15, message = "Пароль должен быть от 2 до 15 символов")
    @Column(name = "password")
    private  String password;

    public User() {}

    public User(String firstName, String lastName, String email, int age, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}