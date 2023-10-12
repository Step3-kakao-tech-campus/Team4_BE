package com.ktc.matgpt.feature_review.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockStoreJPARepository extends JpaRepository<MockStore,Long> {

}
