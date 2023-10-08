package com.ktc.matgpt.feature_review.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageJPARepository extends JpaRepository<Image, Long> {

}
