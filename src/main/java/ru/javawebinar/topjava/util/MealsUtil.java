package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenInclusive;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
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

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
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

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate,Integer> caloriesToday = meals.stream()
                                                    .collect(Collectors.groupingBy(Meal::getDate
                                                            ,Collectors.summingInt(Meal::getCalories)));

        return meals.stream()
                .filter(userMeal -> isBetweenInclusive(userMeal.getTime(),startTime,endTime))
                .map(userMeal -> createTo(userMeal
                        ,caloriesToday.get(userMeal.getDate())>caloriesPerDay))
                .collect(Collectors.toList());
    }
    public static MealTo createTo (Meal meal, boolean excess){
        return new MealTo(meal.getDateTime(),meal.getDescription(),meal.getCalories(),excess);
    }
}
