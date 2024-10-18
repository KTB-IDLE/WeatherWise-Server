package com.idle.weather.level.service;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.api.port.LevelService;
import com.idle.weather.level.api.response.LevelResponseDto;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final UserRepository userRepository;

    @Override
    public LevelResponseDto.RankingList getRankingList(Long userId) {
        // TODO: 10/9/24 User 부분 수정

        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        int currentUserRanking = userRepository.findUserRanking(user.getLevel());

        List<User> userList = userRepository.findTop10ByOrderByLevelDesc();
        List<LevelResponseDto.SingleRanking> rankingList = userList.stream()
                .map(LevelResponseDto.SingleRanking::from).toList();

        return LevelResponseDto.RankingList.of(rankingList , currentUserRanking , user.getNickname(), user.getLevel());
    }
}

