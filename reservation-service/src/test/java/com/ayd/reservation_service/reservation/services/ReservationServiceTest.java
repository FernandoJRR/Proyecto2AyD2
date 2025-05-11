package com.ayd.reservation_service.reservation.services;

import com.ayd.reservation_service.reservation.dtos.CreateReservationRequestDTO;
import com.ayd.reservation_service.reservation.dtos.ReservationSpecificationRequestDTO;
import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.reservation_service.reservation.repositories.ReservationRepository;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private static final String RESERVATION_ID = "resv-123";
    private static final String USER_ID = "user-456";
    private static final LocalDate DATE = LocalDate.of(2025, 5, 10);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(11, 0);
    private static final boolean ONLINE = true;
    private static final boolean PAID = false;
    private static final boolean CANCELLED = false;

    private CreateReservationRequestDTO createDTO;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        createDTO = new CreateReservationRequestDTO(START_TIME, END_TIME, DATE, USER_ID, ONLINE);
        reservation = new Reservation(createDTO);
        reservation.setId(RESERVATION_ID);
        reservation.setPaid(PAID);
        reservation.setCancelled(CANCELLED);
    }

    /**
     * dado: no existe una reserva con la misma configuración ni por usuario.
     * cuando: se llama a createReservation.
     * entonces: se guarda correctamente la reserva y se retorna.
     */
    @Test
    void shouldCreateReservationSuccessfully() throws DuplicatedEntryException {
        when(reservationRepository.existsByStartTimeAndEndTimeAndDateAndUserIdAndOnline(
                START_TIME, END_TIME, DATE, USER_ID, ONLINE)).thenReturn(false);
        when(reservationRepository.existsByStartTimeAndEndTimeAndDateAndOnline(
                START_TIME, END_TIME, DATE, ONLINE)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.createReservation(createDTO);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        assertEquals(START_TIME, result.getStartTime());
        assertEquals(END_TIME, result.getEndTime());
        assertEquals(DATE, result.getDate());
        assertEquals(ONLINE, result.isOnline());

        verify(reservationRepository).save(any(Reservation.class));
    }

    /**
     * dado: ya existe una reserva con la misma configuración y el mismo usuario.
     * cuando: se llama a createReservation.
     * entonces: se lanza una DuplicatedEntryException.
     */
    @Test
    void shouldThrowWhenDuplicateReservationByUserExists() {
        when(reservationRepository.existsByStartTimeAndEndTimeAndDateAndUserIdAndOnline(
                START_TIME, END_TIME, DATE, USER_ID, ONLINE)).thenReturn(true);

        assertThrows(DuplicatedEntryException.class,
                () -> reservationService.createReservation(createDTO));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: no existe reserva duplicada por usuario, pero sí una reserva general
     * con misma configuración.
     * cuando: se llama a createReservation.
     * entonces: se lanza una DuplicatedEntryException.
     */
    @Test
    void shouldThrowWhenDuplicateReservationGeneralExists() {
        when(reservationRepository.existsByStartTimeAndEndTimeAndDateAndUserIdAndOnline(
                START_TIME, END_TIME, DATE, USER_ID, ONLINE)).thenReturn(false);
        when(reservationRepository.existsByStartTimeAndEndTimeAndDateAndOnline(
                START_TIME, END_TIME, DATE, ONLINE)).thenReturn(true);

        assertThrows(DuplicatedEntryException.class,
                () -> reservationService.createReservation(createDTO));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: existe una reserva válida que no ha sido cancelada ni pagada.
     * cuando: se llama a cancelReservation.
     * entonces: se marca como cancelada y se guarda correctamente.
     */
    @Test
    void shouldCancelReservationSuccessfully() throws NotFoundException {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.cancelReservation(RESERVATION_ID);

        assertTrue(result.isCancelled());
        verify(reservationRepository).save(reservation);
    }

    /**
     * dado: no existe una reserva con el ID proporcionado.
     * cuando: se llama a cancelReservation.
     * entonces: se lanza NotFoundException.
     */
    @Test
    void shouldThrowNotFoundWhenReservationDoesNotExist() {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.cancelReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: la reserva ya ha sido pagada.
     * cuando: se llama a cancelReservation.
     * entonces: se lanza IllegalStateException.
     */
    @Test
    void shouldThrowWhenReservationAlreadyPaid() {
        reservation.setPaid(true);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.cancelReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: la reserva ya ha sido cancelada previamente.
     * cuando: se llama a cancelReservation.
     * entonces: se lanza IllegalStateException.
     */
    @Test
    void shouldThrowWhenReservationAlreadyCancelled() {
        reservation.setCancelled(true);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.cancelReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: existe una reserva válida que no ha sido pagada ni cancelada.
     * cuando: se llama a setPaymentReservation.
     * entonces: se marca como pagada y se guarda correctamente.
     */
    @Test
    void shouldSetPaymentSuccessfully() throws NotFoundException {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.setPaymentReservation(RESERVATION_ID);

        assertTrue(result.isPaid());
        verify(reservationRepository).save(reservation);
    }

    /**
     * dado: no existe una reserva con el ID proporcionado.
     * cuando: se llama a setPaymentReservation.
     * entonces: se lanza NotFoundException.
     */
    @Test
    void shouldThrowNotFoundWhenSettingPaymentOnNonexistentReservation() {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.setPaymentReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: la reserva ya ha sido pagada.
     * cuando: se llama a setPaymentReservation.
     * entonces: se lanza IllegalStateException.
     */
    @Test
    void shouldThrowWhenReservationAlreadyPaidOnSetPayment() {
        reservation.setPaid(true);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.setPaymentReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: la reserva ha sido cancelada.
     * cuando: se llama a setPaymentReservation.
     * entonces: se lanza IllegalStateException.
     */
    @Test
    void shouldThrowWhenReservationCancelledOnSetPayment() {
        reservation.setCancelled(true);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.setPaymentReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: existe una reserva válida no pagada.
     * cuando: se llama a deleteReservation.
     * entonces: se elimina la reserva correctamente y se retorna true.
     */
    @Test
    void shouldDeleteReservationSuccessfully() throws NotFoundException {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        boolean result = reservationService.deleteReservation(RESERVATION_ID);

        assertTrue(result);
        verify(reservationRepository).delete(reservation);
    }

    /**
     * dado: no existe una reserva con el ID dado.
     * cuando: se llama a deleteReservation.
     * entonces: se lanza NotFoundException.
     */
    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonexistentReservation() {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.deleteReservation(RESERVATION_ID));

        verify(reservationRepository, never()).delete((Reservation) any());
    }

    /**
     * dado: la reserva ya fue pagada.
     * cuando: se llama a deleteReservation.
     * entonces: se lanza IllegalStateException y no se elimina la reserva.
     */
    @Test
    void shouldThrowWhenDeletingPaidReservation() {
        reservation.setPaid(true);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.deleteReservation(RESERVATION_ID));

        verify(reservationRepository, never()).delete((Reservation) any());
    }

    /**
     * dado: el parámetro reservationSpecificationRequestDTO es null.
     * cuando: se llama a getReservations.
     * entonces: se retorna la lista completa de reservas.
     */
    @Test
    void shouldReturnAllReservationsWhenSpecificationIsNull() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservations(null);

        assertEquals(1, result.size());
        assertEquals(RESERVATION_ID, result.get(0).getId());
        verify(reservationRepository).findAll();
    }

    /**
     * dado: reservationSpecificationRequestDTO tiene filtros válidos.
     * cuando: se llama a getReservations.
     * entonces: se aplica la specification y se retorna la lista filtrada.
     */
    @Test
    void shouldReturnFilteredReservationsWhenSpecificationProvided() {
        ReservationSpecificationRequestDTO dto = new ReservationSpecificationRequestDTO(
                USER_ID, START_TIME, END_TIME, LocalDate.now(), true, false, false);

        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAll(any(Specification.class))).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservations(dto);

        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).getUserId());
        verify(reservationRepository).findAll(any(Specification.class));
    }

}