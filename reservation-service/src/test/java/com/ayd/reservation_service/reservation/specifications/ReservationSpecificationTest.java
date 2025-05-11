package com.ayd.reservation_service.reservation.specifications;

import com.ayd.reservation_service.reservation.models.Reservation;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationSpecificationTest {

    private static final String USER_ID = "user-123";
    private static final LocalDate DATE = LocalDate.of(2025, 5, 20);
    private static final LocalTime START_TIME = LocalTime.of(14, 0);
    private static final LocalTime END_TIME = LocalTime.of(15, 0);
    private static final Boolean ONLINE = true;
    private static final Boolean PAID = true;
    private static final Boolean CANCELLED = false;

    @Mock private Root<Reservation> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateForUserId() {
        Path<String> path = mock(Path.class);
        when(root.get("userId")).thenReturn((Path)path);
        when(cb.equal(path, USER_ID)).thenReturn(predicate);

        Predicate result = ReservationSpecification.hasUserId(USER_ID).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullUserId() {
        assertNull(ReservationSpecification.hasUserId(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForDate() {
        Path<LocalDate> path = mock(Path.class);
        when(root.get("date")).thenReturn((Path)path);
        when(cb.equal(path, DATE)).thenReturn(predicate);

        Predicate result = ReservationSpecification.hasDate(DATE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullDate() {
        assertNull(ReservationSpecification.hasDate(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForStartTime() {
        Path<LocalTime> path = mock(Path.class);
        when(root.get("startTime")).thenReturn((Path)path);
        when(cb.equal(path, START_TIME)).thenReturn(predicate);

        Predicate result = ReservationSpecification.hasStartTime(START_TIME).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullStartTime() {
        assertNull(ReservationSpecification.hasStartTime(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForEndTime() {
        Path<LocalTime> path = mock(Path.class);
        when(root.get("endTime")).thenReturn((Path)path);
        when(cb.equal(path, END_TIME)).thenReturn(predicate);

        Predicate result = ReservationSpecification.hasEndTime(END_TIME).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullEndTime() {
        assertNull(ReservationSpecification.hasEndTime(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForOnline() {
        Path<Boolean> path = mock(Path.class);
        when(root.get("online")).thenReturn((Path)path);
        when(cb.equal(path, ONLINE)).thenReturn(predicate);

        Predicate result = ReservationSpecification.isOnline(ONLINE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullOnline() {
        assertNull(ReservationSpecification.isOnline(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForPaid() {
        Path<Boolean> path = mock(Path.class);
        when(root.get("paid")).thenReturn((Path)path);
        when(cb.equal(path, PAID)).thenReturn(predicate);

        Predicate result = ReservationSpecification.isPaid(PAID).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullPaid() {
        assertNull(ReservationSpecification.isPaid(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForCancelled() {
        Path<Boolean> path = mock(Path.class);
        when(root.get("cancelled")).thenReturn((Path)path);
        when(cb.equal(path, CANCELLED)).thenReturn(predicate);

        Predicate result = ReservationSpecification.isCancelled(CANCELLED).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullCancelled() {
        assertNull(ReservationSpecification.isCancelled(null).toPredicate(root, query, cb));
    }
}
