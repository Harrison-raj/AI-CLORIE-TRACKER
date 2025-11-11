package com.example.backend.controller;

import com.example.backend.model.Meal;
import com.example.backend.model.User;
import com.example.backend.repository.MealRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    // Endpoint to predict calories from image
    @PostMapping("/predict")
    public ResponseEntity<String> predict(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) throws IOException {

        String flaskUrl = "http://localhost:8000/predict"; // Flask AI service URL

        // Prepare multipart request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send request to Flask
        ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, requestEntity, Map.class);
        Map<String, Object> result = response.getBody();

        // Find the user
        User loggedInUser= userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save meal linked to user
        Meal meal = new Meal();
        meal.setLabel((String) result.get("label"));
        meal.setCalories(((Number) result.get("calories")).intValue());
        meal.setTimestamp(LocalDateTime.now());
        meal.setUser(loggedInUser);

        mealRepository.save(meal);

        return ResponseEntity.ok(result.toString());
    }

    // Endpoint to get meal history for a user
    @GetMapping("/history")
    public ResponseEntity<List<Meal>> getMealHistory(@RequestParam("email") String email) {
        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Meal> meals = mealRepository.findAll().stream()
                .filter(meal -> meal.getUser().getId().equals(loggedInUser.getId()))
                .toList();
        return ResponseEntity.ok(meals);
    }
}
