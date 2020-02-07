package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenInclusive;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

//        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
//        System.out.println(filteredByCycles(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));


    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<MealTo> filtered = new ArrayList<>();
        for (Meal meal : meals) {
            int calories = 0;
            for (Meal mealPerDay : meals) {
                if (meal.getDate().equals(mealPerDay.getDate())) {
                    calories += mealPerDay.getCalories();
                }
            }
            if (isBetweenInclusive(meal.getTime(), startTime, endTime)) {
                filtered.add(createTo(meal, calories > caloriesPerDay));
            }
        }
        return filtered;
    }

    public static List<MealTo> filteredByStreams(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<MealTo> mealsTo = getTo(meals, caloriesPerDay);
        return mealsTo.stream()
                .filter(mto -> isBetweenInclusive(mto.getTime(), startTime, endTime))
                .collect(Collectors.toList());
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static List<MealTo> getTo(Collection<Meal> meals, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesToday = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate
                        , Collectors.summingInt(Meal::getCalories)));

        return meals.stream()
                .map(userMeal -> createTo(userMeal
                        , caloriesToday.get(userMeal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
