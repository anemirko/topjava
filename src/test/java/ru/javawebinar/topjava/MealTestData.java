package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 6;
    public static final int ADMIN_MEAL_ID = USER_MEAL_ID + 2;

    public static final Meal USER_MEAL = new Meal(USER_MEAL_ID,
            LocalDateTime.of(2018, 10, 2, 13, 0, 0),
            "Обед", 1000);

    public static final Meal ADMIN_MEAL = new Meal(ADMIN_MEAL_ID,
            LocalDateTime.of(2018, 10, 2, 13, 0, 0),
            "Админ Обед", 510);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }


}
