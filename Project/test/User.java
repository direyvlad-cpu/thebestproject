package model;

import java.util.Objects;

public class User implements Comparable<User> {
    private final String name;
    private final String password;
    private final String email;

    private User(Builder builder) {
        this.name = builder.name;
        this.password = builder.password;
        this.email = builder.email;
    }

    public static Builder builder() { return new Builder(); }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    @Override
    public int compareTo(User other) {
        int nameComparison = this.name.compareTo(other.name);
        return nameComparison != 0 ? nameComparison : this.email.compareTo(other.email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && password.equals(user.password) && email.equals(user.email);
    }

    @Override
    public int hashCode() { return Objects.hash(name, password, email); }

    @Override
    public String toString() { return "User{имя=" + name + ", почта=" + email + "}"; }

    /** Пароль намеренно не записывается в экспортный файл. */
    public String toFileString() { return name + ";" + email + ";***"; }

    public boolean checkPassword(String input) { return this.password.equals(input); }

    public static class Builder {
        private String name;
        private String password;
        private String email;

        private Builder() { }
        public Builder name(String value) { this.name = value; return this; }
        public Builder password(String value) { this.password = value; return this; }
        public Builder email(String value) { this.email = value; return this; }

        public User build() {
            name = DataValidator.validateUserName(name);
            password = DataValidator.validatePassword(password);
            email = DataValidator.validateEmail(email);
            return new User(this);
        }
    }
}
