package com.idle.weather.level.service;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.api.port.LevelService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.idle.weather.level.api.response.LevelResponseDto.*;

@Service
@RequiredArgsConstructor
@Builder
public class LevelServiceImpl implements LevelService {

    private final UserRepository userRepository;

    @Override
    public RankingList getRankingList(Long userId) {
        User user = userRepository.findById(userId);

        int currentUserRanking = userRepository.findUserRanking(user.getLevel());

        List<User> userList = userRepository.findTop10ByOrderByLevelDesc();

        List<SingleRanking> rankingList = userList.stream()
                .map(SingleRanking::from).toList();

        return RankingList.of(rankingList , currentUserRanking , user.getNickname(), user.getLevel());
    }
}

