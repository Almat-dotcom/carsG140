package kz.bitlab.carsG140.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import kz.bitlab.carsG140.entity.Car;
import kz.bitlab.carsG140.entity.Category;
import kz.bitlab.carsG140.entity.Country;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CarSpecification {
    public static Specification<Car>    getCarSpecification(String name, Integer minYear, Integer maxPrice,Long countryId, Long categoryId){
        Specification<Car> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name));
            }

            if (minYear != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("year"), minYear));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("price"), maxPrice));
            }

            if (countryId != null) {
                Join<Car, Country> countryJoin = root.join("country");
                predicates.add(criteriaBuilder.equal(countryJoin.get("id"), countryId));
            }

            if (categoryId != null) {
                Join<Car, Category> categoryJoin = root.join("categories");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }

            Predicate commonPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            return commonPredicate;
        };

        return specification;
    }
}
