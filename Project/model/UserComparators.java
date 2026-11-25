package model;

import java.util.Comparator;

public final class UserComparators {

    private UserComparators() { }

    public static final Comparator<User> BY_NAME =
            Comparator.comparing(User::getName);

    public static final Comparator<User> BY_EMAIL =
            Comparator.comparing(User::getEmail);

    public static final Comparator<User> BY_PASSWORD =
            Comparator.comparing(User::getPassword);
}

