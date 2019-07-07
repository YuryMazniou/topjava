package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles({"datajpa","datajpa_jpa"})
public class DataJpaUserTest extends AbstractUserServiceTest {

    @Test
    public void getByUserWithHisMeals() throws Exception{
        User user=getService().getByUserWithHisMeals(100001);
        assertMatch(user.getMeals(),Arrays.asList(MealTestData.ADMIN_MEAL2,MealTestData.ADMIN_MEAL1));
    }

    @Override
    public void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setMeals(MealTestData.MEALS);
        getService().update(updated);
        assertMatch(getService().get(USER_ID), updated);
    }
}
