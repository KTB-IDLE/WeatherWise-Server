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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.idle.weather.level.api.response.LevelResponseDto.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Builder
public class LevelServiceImpl implements LevelService {

    private final UserRepository userRepository;

    /**
     * 10위까지만
     * 검색하면 등수 달라지는거 똑같이 하기
     */
    @Override
    public RankingList getRankingList(Long userId, int page , int size) {
        User user = userRepository.findById(userId);

        int currentUserRanking = userRepository.findUserRanking(user.getLevel() , user.getPoint());

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("level"),
                        Sort.Order.desc("point"),
                        Sort.Order.asc("nickname")
                )
        );
        Page<User> userPage = userRepository.findAllByOrderByLevelDescPointDesc(pageable);
        List<SingleRanking> rankingList = userPage.getContent().stream().map(SingleRanking::from).collect(toList());

        boolean isExistUserInCurrentPage = userPage.getContent().stream().anyMatch(u -> u.getId().equals(userId));

/*        List<User> userList = userRepository.findTop10ByOrderByLevelDescExperienceDesc();
        List<SingleRanking> rankingList = userList.stream()
                .map(SingleRanking::from).toList();
        boolean isTopLevelUser = false;
        if (user.getLevel() <= 10) {
            isTopLevelUser = true;
        }*/
        boolean isTopLevelUser = currentUserRanking <= size * page;
        return RankingList.of(rankingList , currentUserRanking , user.getNickname(),
                user.getLevel(), userPage.hasNext() , userPage.hasPrevious(),isExistUserInCurrentPage);
    }

    @Override
    public SingleRanking getRankingByNickName(String nickName) {
        return userRepository.findByRankFromNickname(nickName);
    }


/*    @Override
    public ExpByLevelResponse getExpByLevel(int level) {
        return ExpByLevelResponse.from(levelJpaRepository.findByLevel(level));
    }*/
}

