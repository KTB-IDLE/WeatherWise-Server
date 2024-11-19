package com.idle.weather.level.service;

import com.idle.weather.level.api.response.LevelResponseDto;
import com.idle.weather.mock.FakeUserRepository;
import com.idle.weather.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.idle.weather.level.api.response.LevelResponseDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LevelServiceTest {
    private LevelServiceImpl levelService;
    private FakeUserRepository fakeUserRepository;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        this.levelService = LevelServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .build();
    }

    @AfterEach
    void clear() {
        fakeUserRepository.clear();
    }

    @Test
    public void 상위_10명의_랭킹_정보와_나의_랭킹_정보를_볼_수_있다() throws Exception
    {
        //given
        // 기존의 유저는 20명이 존재한다.
        initUser(fakeUserRepository);
        User user = User.builder()
                .missionHistories(new ArrayList<>())
                .nickname("REAL CIAN")
                .password("!234")
                .isCompletedSurvey(true)
                .easilyCold(true)
                .easilyHot(false)
                .easilySweat(true)
                .level(1)
                .point(0)
                .build();
        User createUser = fakeUserRepository.save(user);


        //when
        RankingList rankingList = levelService.getRankingList(createUser.getId(),0,10);

        //then
        assertThat(rankingList.getRankingList().size()).isEqualTo(10);
        assertThat(rankingList.getCurrentUserLevel()).isEqualTo(user.getLevel());
    }

    @Test
    public void 랭킹_정보를_가지고_올_때_레벨이_같은_경우에는_경험치를_비교해서_순위를_매긴다() throws Exception
    {
        //given
        User user1 = User.builder()
                .missionHistories(new ArrayList<>())
                .nickname("REAL CIAN1")
                .password("!234")
                .isCompletedSurvey(true)
                .easilyCold(true)
                .easilyHot(false)
                .easilySweat(true)
                .level(3)
                .point(10)
                .build();

        User user2 = User.builder()
                .missionHistories(new ArrayList<>())
                .nickname("REAL CIAN2")
                .password("!234")
                .isCompletedSurvey(true)
                .easilyCold(true)
                .easilyHot(false)
                .easilySweat(true)
                .level(3)
                .point(20)
                .build();
        User createUser1 = fakeUserRepository.save(user1);
        User createUser2 = fakeUserRepository.save(user2);

        //when
        RankingList rankingList = levelService.getRankingList(createUser1.getId(),0,10);

        //then
        assertThat(rankingList.getRankingList().size()).isEqualTo(2);
        assertThat(rankingList.getRankingList().get(0).getNickName()).isEqualTo("REAL CIAN2");
        assertThat(rankingList.getRankingList().get(1).getNickName()).isEqualTo("REAL CIAN1");
    }

    @Test
    public void 해당_유저가_상위_10위_안에_든다면_isTopLevelUser_가_true_이다() {
        //given
        User user1 = User.builder()
                .missionHistories(new ArrayList<>())
                .nickname("REAL CIAN1")
                .password("!234")
                .isCompletedSurvey(true)
                .easilyCold(true)
                .easilyHot(false)
                .easilySweat(true)
                .level(3)
                .point(10)
                .build();

        User user2 = User.builder()
                .missionHistories(new ArrayList<>())
                .nickname("REAL CIAN2")
                .password("!234")
                .isCompletedSurvey(true)
                .easilyCold(true)
                .easilyHot(false)
                .easilySweat(true)
                .level(3)
                .point(20)
                .build();
        User createUser1 = fakeUserRepository.save(user1);
        User createUser2 = fakeUserRepository.save(user2);

        //when
        RankingList rankingList = levelService.getRankingList(createUser1.getId(),0,10);

        //then
        assertThat(rankingList.isTopLevelUser()).isTrue();
        assertThat(rankingList.getCurrentUserRanking()).isLessThanOrEqualTo(10);
    }

    private static void initUser(FakeUserRepository fakeUserRepository) {
        for (int i = 1; i <= 20; i++) {
            User user1 = User.builder()
                    .missionHistories(new ArrayList<>())
                    .nickname("CIAN" + i)
                    .password("!234" + i)
                    .isCompletedSurvey(true)
                    .easilyCold(true)
                    .easilyHot(false)
                    .easilySweat(true)
                    .level(i)
                    .point(0)
                    .build();
            fakeUserRepository.save(user1);
        }
    }

}