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

    //거리안의 음식점 marker 가져오기
    @Query(value ="SELECT * FROM store_tb WHERE latitude < :maxLat AND latitude > :minLat AND longitude < :maxLon AND longitude > :minLon  ", nativeQuery = true)
    List<Store> findMarkers(@Param("maxLat")double maxLat, @Param("maxLon")double maxLon, @Param("minLat")double minLat, @Param("minLon")double minLon);

    //사용자의 위치와 음식점 거리가 가까운 순으로 음식점 불러오기
    @Query(value ="SELECT *,ST_Distance_Sphere(POINT(:longitude, :latitude),POINT(longitude, latitude)) AS distance FROM store_tb ", nativeQuery = true)
    List<Store> findNearestStoresWithDistance(double latitude, double longitude);


    //별점 높은 순으로 가게 불러오기
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:search% ORDER BY s.ratingAvg DESC , s.id")
    List<Store> findAllByStar(@Param("search")String search,Pageable page);

    //별점 높은 순으로 가게 불러오기 with cursor id
    @Query("SELECT s FROM Store s WHERE s.id < :id AND s.name LIKE %:search% ORDER by s.ratingAvg DESC, s.id ")
    List<Store> findAllByStarLessThanIdDesc(@Param("search")String search,@Param("id")Long id, Pageable page);


    //id 내림차순으로 가게 불러오기
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:search% ORDER by s.id DESC ")
    List<Store> findAllById(@Param("search")String search,Pageable page);

    //id 내림차순으로 가게 불러오기 with cursor id
    @Query("SELECT s FROM Store s WHERE s.id < :id AND s.name LIKE %:search% ORDER by  s.id  ")
    List<Store> findByIdLessThanOrderByIdDesc(@Param("search")String search,Long id, Pageable page);

    //리뷰 많은 순으로 가게 불러오기
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:search% ORDER BY s.numsOfReview DESC, s.id ")
    List<Store> findAllByReviews(@Param("search")String search,  Pageable page);

    //리뷰 많은 순으로 가게 불러오기 with cursor id
    @Query("SELECT s FROM Store s WHERE s.id < :id AND s.name LIKE %:search% ORDER by s.numsOfReview DESC, s.id  ")
    List<Store> findAllByReviewsLessThanIdDesc(@Param("search")String search,@Param("id")Long id, Pageable page);
}