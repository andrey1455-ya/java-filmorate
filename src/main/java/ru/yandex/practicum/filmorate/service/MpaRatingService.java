//package ru.yandex.practicum.filmorate.service;
//
//import org.springframework.stereotype.Service;
//import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
//import ru.yandex.practicum.filmorate.storage.MpaRatingRepository;
//
//import java.util.List;
//
//@Service
//public class MpaRatingService {
//    private final MpaRatingRepository mpaRatingRepository;
//
//    public MpaRatingService(MpaRatingRepository mpaRatingRepository) {
//        this.mpaRatingRepository = mpaRatingRepository;
//    }
//
//    public List<MpaRatingDto> findAllRatings() {
//        return mpaRatingRepository.findAllRatings().stream()
//                .map(MpaRatingMapper::mapToMpaRatingDto)
//                .toList();
//    }
//
//    public MpaRatingDto findRatingById(Long id) {
//        return mpaRatingRepository.findRatingById(id)
//                .map(MpaRatingMapper::mapToMpaRatingDto)
//                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг с id=%d не найден", id)));
//    }
//}
package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.storage.MpaRatingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MpaRatingService {
    private final MpaRatingRepository mpaRatingRepository;

    public MpaRatingService(MpaRatingRepository mpaRatingRepository) {
        this.mpaRatingRepository = mpaRatingRepository;
    }

    public List<MpaRatingDto> getAllRatings() {
        log.info("Запрос на получение всех рейтингов MPA");
        return mpaRatingRepository.getAllRatings().stream()
                .map(MpaRatingMapper::mapToMpaRatingDto)
                .collect(Collectors.toList());
    }

    public MpaRatingDto getRatingById(Long id) {
        log.info("Запрос на получение рейтинга MPA с ID: {}", id);
        return mpaRatingRepository.getRatingById(id)
                .map(MpaRatingMapper::mapToMpaRatingDto)
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг с id=%d не найден", id)));
    }
}