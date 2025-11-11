package com.example.backend.controller;

import com.example.backend.model.Meal;
import com.example.backend.model.User;
import com.example.backend.repository.MealRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all meals for a specific user
    @GetMapping("/{userId}")
    public ResponseEntity<List<Meal>> getMealsByUserId(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
        List<Meal> meals = user.getMeals(); // Assuming User has getMeals() method
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    // Add a new meal
    @PostMapping("/{userId}")
    public ResponseEntity<Meal> addMeal(@PathVariable Long userId, @RequestBody Meal meal) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
        meal.setUser(user);
        meal.setTimestamp(LocalDateTime.now());
        Meal savedMeal = mealRepository.save(meal);
        return new ResponseEntity<>(savedMeal, HttpStatus.CREATED);
    }

    // Update an existing meal
    @PutMapping("/{userId}/{mealId}")
    public ResponseEntity<Meal> updateMeal(@PathVariable Long userId, @PathVariable Long mealId, @RequestBody Meal meal) {
        Optional<Meal> existingMealOptional = mealRepository.findById(mealId);
        if (existingMealOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Meal existingMeal = existingMealOptional.get();
        if (!existingMeal.getUser().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        existingMeal.setLabel(meal.getLabel());
        existingMeal.setCalories(meal.getCalories());
        Meal updatedMeal = mealRepository.save(existingMeal);
        return new ResponseEntity<>(updatedMeal, HttpStatus.OK);
    }

    // Delete a meal
    @DeleteMapping("/{userId}/{mealId}")
    public ResponseEntity<HttpStatus> deleteMeal(@PathVariable Long userId, @PathVariable Long mealId) {
        Optional<Meal> existingMealOptional = mealRepository.findById(mealId);
        if (existingMealOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!existingMealOptional.get().getUser().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        mealRepository.deleteById(mealId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
