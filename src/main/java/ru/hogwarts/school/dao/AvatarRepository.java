package ru.hogwarts.school.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.SessionScope;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

@Repository
@SessionScope
public interface AvatarRepository extends PagingAndSortingRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(Long studentId);
}
