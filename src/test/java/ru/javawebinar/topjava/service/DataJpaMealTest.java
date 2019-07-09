package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.concurrent.TimeUnit;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles({"datajpa","datajpa_jpa"})
public class DataJpaMealTest extends AbstractMealServiceTest {

    @Test
    public void getByMealWithUser(){
        Meal m=getService().getByMealWithUser(MealTestData.MEAL1_ID, UserTestData.USER_ID);
        assertMatch(m, MEAL1);
        assertMatch(USER,m.getUser());
    }

    @Test
    public void getByMealWithUserNotFound(){
        thrown.expect(NotFoundException.class);
        getService().getByMealWithUser(MealTestData.MEAL1_ID, ADMIN_ID);
    }

    private static StringBuilder results = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }
}
