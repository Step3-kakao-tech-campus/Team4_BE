package com.ktc.matgpt.store;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreJPARepository extends JpaRepository<Store,Long> {

}
