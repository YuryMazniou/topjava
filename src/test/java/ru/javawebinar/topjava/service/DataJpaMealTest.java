package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

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

    @Test(expected = NotFoundException.class)
    public void getByMealWithUserNotFound(){
        getService().getByMealWithUser(MealTestData.MEAL1_ID, ADMIN_ID);
    }
}
