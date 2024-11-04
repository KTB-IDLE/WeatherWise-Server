package com.idle.weather.mock;

import com.idle.weather.user.domain.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeUserJpaRepository {
    private static Long id = 0L;
    private final List<User> data = new ArrayList<>();
}
