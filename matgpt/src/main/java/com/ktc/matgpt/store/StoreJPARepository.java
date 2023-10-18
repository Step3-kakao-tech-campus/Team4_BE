package com.ktc.matgpt.store;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreJPARepository extends JpaRepository<Store,Long> {

    Optional<Store> findById(Long id);

    @Query(value ="SELECT s.id, s.storeName, s.category, s.ratingAvg, s.numsOfReview,ST_Distance_Sphere(POINT(:longitude, :latitude),POINT(s.latitude, s.longitude)) AS distance FROM Store s ", nativeQuery = true)
    List<Store> findNearestStoresWithDistance(double latitude, double longitude);

    //별점 높은 순으로 가게 불러오기
    @Query("SELECT s FROM Store s ORDER BY s.ratingAvg DESC , s.id")
    List<Store> findAllByStar(Pageable page);

    //별점 높은 순으로 가게 불러오기 with cursor id
    @Query("SELECT s FROM Store s WHERE s.id < :id ORDER by s.ratingAvg DESC, s.id ")
    List<Store> findAllByStarLessThanIdDesc(@Param("id")Long id, Pageable page);


    //id 내림차순으로 가게 불러오기
    @Query("SELECT s FROM Store s ORDER by s.id DESC ")
    List<Store> findAllById(Pageable page);

    //id 내림차순으로 가게 불러오기 with cursor id
    List<Store> findByIdLessThanOrderByIdDesc(Long id, Pageable page);

    //리뷰 많은 순으로 가게 불러오기
    @Query("SELECT s FROM Store s ORDER BY s.numsOfReview DESC, s.id ")
    List<Store> findAllByReviews(Pageable page);

    //리뷰 많은 순으로 가게 불러오기 with cursor id
    @Query("SELECT s FROM Store s WHERE s.id < :id ORDER by s.numsOfReview DESC, s.id  ")
    List<Store> findAllByReviewsLessThanIdDesc(@Param("id")Long id, Pageable page);
}