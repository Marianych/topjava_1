package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.Util.isBetweenInclusive;

public final class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    private MealsUtil() {
    }

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

    public static List<MealTo> getFilteredTo(Collection<Meal> meals, @Nullable LocalTime startTime, @Nullable LocalTime endTime, int caloriesPerDay) {
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
