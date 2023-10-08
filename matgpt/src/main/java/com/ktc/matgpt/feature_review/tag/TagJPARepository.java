package com.ktc.matgpt.feature_review.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagJPARepository extends JpaRepository<Tag, Long> {
    @Query("select t from Tag t where t.image.id = :imageId")
    List<Tag> findAllByImageId(Long imageId);

}
