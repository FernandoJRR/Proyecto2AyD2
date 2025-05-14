package com.ayd.reservation_service.reservation.specifications;

import com.ayd.reservation_service.reservation.models.Reservation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationSpecification {

    public static Specification<Reservation> hasUserId(String userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.get("userId"), userId);
    }

    public static Specification<Reservation> hasDate(LocalDate date) {
        return (root, query, cb) -> date == null ? null : cb.equal(root.get("date"), date);
    }

    public static Specification<Reservation> hasStartTime(LocalTime startTime) {
        return (root, query, cb) -> startTime == null ? null : cb.equal(root.get("startTime"), startTime);
    }

    public static Specification<Reservation> hasEndTime(LocalTime endTime) {
        return (root, query, cb) -> endTime == null ? null : cb.equal(root.get("endTime"), endTime);
    }

    public static Specification<Reservation> isOnline(Boolean online) {
        return (root, query, cb) -> online == null ? null : cb.equal(root.get("online"), online);
    }

    public static Specification<Reservation> isPaid(Boolean paid) {
        return (root, query, cb) -> paid == null ? null : cb.equal(root.get("paid"), paid);
    }

    public static Specification<Reservation> isCancelled(Boolean cancelled) {
        return (root, query, cb) -> cancelled == null ? null : cb.equal(root.get("cancelled"), cancelled);
    }
}
