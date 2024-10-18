package com.idle.weather.level.service;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.api.port.LevelService;
import com.idle.weather.level.api.response.LevelResponseDto;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.idle.weather.level.api.response.LevelResponseDto.*;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final UserRepository userRepository;

    @Override
    public RankingList getRankingList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        int currentUserRanking = userRepository.findUserRanking(user.getLevel());

        // TODO: 10/18/24 UserEntity -> Domain 으로 수정하기
        List<UserEntity> userList = userRepository.findTop10ByOrderByLevelDesc();
        List<SingleRanking> rankingList = userList.stream()
                .map(SingleRanking::from).toList();

        return RankingList.of(rankingList , currentUserRanking , user.getNickname(), user.getLevel());
    }
}

