package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }
    @Autowired
    private MealService service;
    @Test
    public void get() {
        Meal meal=service.get(1,USER_ID);
        assertMatch(meal,MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception{
        Meal meal=service.get(2,ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(4,ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID),MEAL5,MEAL6);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception{
        service.delete(4,USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal>test=service.getBetweenDates(LocalDate.of(1, 1, 1)
                ,LocalDate.of(3000, 1, 1),USER_ID);
        assertMatch(test,MEAL2,MEAL3,MEAL1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal>test=service.getBetweenDateTimes(LocalDateTime.of(2015,Month.MAY,30,9,0,0)
                ,LocalDateTime.of(2015,Month.MAY,30,11,0,0),USER_ID);
        assertMatch(test,MEAL1);
    }

    @Test
    public void getAll() {
        List<Meal>test=service.getAll(USER_ID);
        assertMatch(test,MEAL2,MEAL3,MEAL1);
    }

    @Test
    public void update() {
        Meal mealUpdate=new Meal(MEAL1);
        mealUpdate.setDescription("dinner");
        mealUpdate.setCalories(1);
        service.update(mealUpdate,USER_ID);
        assertMatch(service.get(1,USER_ID),mealUpdate);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        service.update(MEAL5,USER_ID);
    }
    @Test
    public void create() {
        Meal meal=new Meal(null, LocalDateTime.of(2015, Month.MAY,31,10,00,00),"обед",1000);
        Meal createMeal=service.create(meal,USER_ID);
        meal.setId(createMeal.getId());
        assertMatch(service.getAll(USER_ID),meal,MEAL2,MEAL3,MEAL1);
    }
}