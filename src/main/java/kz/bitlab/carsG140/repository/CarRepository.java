package kz.bitlab.carsG140.repository;

import kz.bitlab.carsG140.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findAll(Specification<Car> specification, Pageable pageRequest);

    List<Car> findAllByYear(int year);
    //Share Project on GitHub

    List<Car> findAllByNameAndYearGreaterThanAndPriceLessThan(String name, Integer year, Integer price);

    List<Car> findAllByNameAndPriceLessThan(String name, Integer price);

    List<Car> findAllByNameAndYearGreaterThan(String name, Integer year);

    List<Car> findAllByNameContainsIgnoreCaseAndPriceGreaterThan(String letter, int price);

    @Query("SELECT c from Car c " +
            "where c.name=:name and c.name is not null and c.year>:minYear and c.price<:minPrice")
    List<Car> findAllBySomeParam(String name, Integer minYear, Integer minPrice);

    @Query(value = "select * from t_cars " +
            "where year>:year and name=:name or price<=:price" , nativeQuery = true)
    List<Car> findAllBySomeParamByNativeQuery(Integer year,String name, Integer price );


}
