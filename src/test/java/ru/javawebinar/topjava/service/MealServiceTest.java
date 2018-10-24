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
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

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
        Meal meal = service.get(USER_MEAL_ID, USER_ID);
        assertMatch(meal, USER_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), service.get(ADMIN_MEAL_ID + 1, ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(USER_MEAL_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate dt = LocalDate.of(2018, 10, 2);
        List<Meal> betweenDates = service.getBetweenDates(dt, dt, USER_ID);
        assertMatch(betweenDates, service.get(USER_MEAL_ID + 1, USER_ID), USER_MEAL, service.get(USER_MEAL_ID - 1, USER_ID));
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime start = LocalDateTime.of(2018, 10, 1, 22, 0);
        LocalDateTime end = LocalDateTime.of(2018, 10, 2, 14, 0);
        List<Meal> betweenDateTimes = service.getBetweenDateTimes(start, end, USER_ID);
        assertMatch(betweenDateTimes, USER_MEAL, service.get(USER_MEAL_ID - 1, USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> adminMeals = service.getAll(ADMIN_ID);
        assertMatch(adminMeals, service.get(ADMIN_MEAL_ID + 1, ADMIN_ID), ADMIN_MEAL);
        List<Meal> userMeals = service.getAll(USER_ID);
        assertMatch(userMeals,
                service.get(USER_MEAL_ID + 1, USER_ID),
                USER_MEAL,
                service.get(USER_MEAL_ID - 1, USER_ID),
                service.get(USER_MEAL_ID - 2, USER_ID),
                service.get(USER_MEAL_ID - 3, USER_ID),
                service.get(USER_MEAL_ID - 4, USER_ID));
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL);
        updated.setDescription("Updated");
        service.update(updated, USER_ID);
        assertMatch(updated, service.get(USER_MEAL_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updated = new Meal(USER_MEAL);
        updated.setDescription("Updated");
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2018, 10, 20, 7, 0, 0), "Завтрак", 250);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
    }
}