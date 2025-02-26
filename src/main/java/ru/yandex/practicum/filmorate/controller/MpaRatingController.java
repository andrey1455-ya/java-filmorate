
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @GetMapping
    public List<MpaRatingDto> getAllRatings() {
        log.info("Запрос на получение всех рейтингов MPA");
        return mpaRatingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaRatingDto> getRatingById(@PathVariable Long id) {
        log.info("Запрос на получение рейтинга MPA с ID: {}", id);
        return ResponseEntity.ok(mpaRatingService.getRatingById(id));
    }
}