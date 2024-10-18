package com.idle.weather.level.service;

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
    public LevelResponseDto.RankingList getRankingList() {
        // TODO: 10/9/24 User 부분 수정
        // 자기 Ranking
        int currentUserRanking = userRepository.findUserRanking(userDomain.getLevel());

        List<User> userList = userRepository.findTop10ByOrderByLevelDesc();
        List<LevelResponseDto.SingleRanking> rankingList = userList.stream().map(LevelResponseDto.SingleRanking::from).toList();

        return LevelResponseDto.RankingList.of(rankingList , currentUserRanking , userDomain.getNickName(), userDomain.getLevel());
    }

    private User getUser() {
        return userRepository.findById(1L).get();
    }
}

