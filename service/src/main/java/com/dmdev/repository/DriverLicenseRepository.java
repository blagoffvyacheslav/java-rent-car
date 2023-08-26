package com.dmdev.repository;

import com.dmdev.dto.DriverLicenseFilter;
import com.dmdev.entity.DriverLicense;
import com.dmdev.utils.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.QDriverLicense.driverLicense;
import static com.dmdev.entity.QUserDetails.userDetails;
import org.springframework.stereotype.Repository;

@Repository
public class DriverLicenseRepository extends BaseRepository<Long, DriverLicense> {

    public DriverLicenseRepository() {
        super(DriverLicense.class);
    }

    public List<DriverLicense> findAllQueryDsl() {
        return new JPAQuery<DriverLicense>(getEntityManager())
                .select(driverLicense)
                .from(driverLicense)
                .fetch();
    }


    public Optional<DriverLicense> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<DriverLicense>(getEntityManager())
                .select(driverLicense)
                .from(driverLicense)
                .where(driverLicense.id.eq(id))
                .fetchOne());
    }


    public List<DriverLicense> findDriverLicensesByIssueAndExpiredDateQueryDsl(DriverLicenseFilter driverLicenseFilter) {
        var predicateIssueDte = QPredicate.builder()
                .add(driverLicenseFilter.getIssueDate(), driverLicense.issueDate::eq)
                .add(driverLicenseFilter.getIssueDate(), driverLicense.issueDate::gt)
                .buildOr();

        var predicateExpiredDate = QPredicate.builder()
                .add(driverLicenseFilter.getExpiredDate(), driverLicense.expiredDate::eq)
                .add(driverLicenseFilter.getExpiredDate(), driverLicense.expiredDate::lt)
                .buildOr();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateIssueDte)
                .addPredicate(predicateExpiredDate)
                .buildAnd();

        return new JPAQuery<DriverLicense>(getEntityManager())
                .select(driverLicense)
                .from(driverLicense)
                .where(predicateAll)
                .fetch();
    }


    public List<Tuple> findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(LocalDate expiredDate) {
        var predicate = QPredicate.builder()
                .add(expiredDate, driverLicense.expiredDate::eq)
                .add(expiredDate, driverLicense.expiredDate::lt)
                .buildOr();

        return new JPAQuery<Tuple>(getEntityManager())
                .select(userDetails.name, userDetails.lastname,
                        driverLicense.number, driverLicense.issueDate,
                        driverLicense.expiredDate)
                .from(driverLicense)
                .join(driverLicense.userDetails, userDetails)
                .where(predicate)
                .orderBy(userDetails.lastname.asc())
                .fetch();
    }
}