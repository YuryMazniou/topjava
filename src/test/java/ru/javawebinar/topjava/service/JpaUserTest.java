package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles({"jpa","datajpa_jpa"})
public class JpaUserTest extends AbstractUserServiceTest {
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
