package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetween;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        System.out.println("By cycles:");
        List<UserMealWithExceed> fwe = getFilteredWithExceededByCycle(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        fwe.forEach(System.out::println);
        System.out.println("By streams:");
        fwe = getFilteredWithExceededByStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        fwe.forEach(System.out::println);
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> sumByDate = new HashMap<>();
        mealList.forEach(um -> {
            sumByDate.putIfAbsent(um.getDate(), 0);
            sumByDate.put(um.getDate(), sumByDate.get(um.getDate()) + um.getCalories());
        });

        List<UserMealWithExceed> result = new ArrayList<>();
        mealList.forEach(um -> {
            if (isBetween(um.getTime(), startTime, endTime)) {
                result.add(createUserMealWithExceed(um, sumByDate.get(um.getDate()) > caloriesPerDay));
            }
        });
        return result;
    }

    /*optional 1 hw0 task*/
    public static List<UserMealWithExceed>  getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> sumByDate = mealList.stream().collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter(um -> isBetween(um.getTime(), startTime, endTime))
                .map(um -> createUserMealWithExceed(um, sumByDate.get(um.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static UserMealWithExceed createUserMealWithExceed(UserMeal um, boolean exceed) {
        return new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(), exceed);
    }
}
