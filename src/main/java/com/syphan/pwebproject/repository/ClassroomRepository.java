package com.syphan.pwebproject.repository;

import com.syphan.pwebproject.model.entity.Classroom;
import com.syphan.pwebproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    Optional<Classroom> findByCode(String code);

    Optional<Classroom> findAllByOwner(User userEntity);

    List<Classroom> findAllByUsers(User userEntity);
}
