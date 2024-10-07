package com.idle.weather.boardvote.repository;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardVoteJpaRepository extends JpaRepository<BoardVoteEntity , Long> {
}
