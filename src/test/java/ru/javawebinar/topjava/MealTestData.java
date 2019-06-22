package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ+1;

    public static final Meal MEAL1=new Meal(1, LocalDateTime.of(2015, Month.MAY,30,10,0,0),"завтрак",500);
    public static final Meal MEAL2=new Meal(2, LocalDateTime.of(2015, Month.MAY,30,19,0,0),"ужин",500);
    public static final Meal MEAL3=new Meal(3, LocalDateTime.of(2015, Month.MAY,30,13,0,0),"обед",1000);
    public static final Meal MEAL4=new Meal(4, LocalDateTime.of(2015, Month.MAY,29,10,0,0),"завтрак",500);
    public static final Meal MEAL5=new Meal(5, LocalDateTime.of(2015, Month.MAY,29,19,0,0),"ужин",510);
    public static final Meal MEAL6=new Meal(6, LocalDateTime.of(2015, Month.MAY,29,13,0,0),"обед",1000);

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
