package com.softserveinc.ita.homeprojectblog.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository<E, I> extends Repo<E, I> {

    Optional<E> findOneByPostIdAndId(I postId, I id);

    Optional<E> findByUserIdAndId(I id, I id1);

}
