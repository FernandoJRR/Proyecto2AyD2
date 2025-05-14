package com.ayd.reservation_service.reservation.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;
import com.ayd.game_service_common.players.dtos.CreatePlayerRequestDTO;
import com.ayd.reservation_service.qr.services.QrCodeAdapter;
import com.ayd.reservation_service.reservation.dtos.CreateReservationRequestDTO;
import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.reservation_service.reservation.ports.ForGameClientPort;
import com.ayd.reservation_service.reservation.repositories.ReservationRepository;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.shared.security.AppProperties;
import com.ayd.sharedReservationService.dto.ReservationSpecificationRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ForGameClientPort gameClientPort;
    @Mock
    private QrCodeAdapter qrCodeAdapter;
    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private ReservationService reservationService;

    private static final String RESERVATION_ID = "resv-123";
    private static final LocalDate DATE = LocalDate.of(2025, 5, 10);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(11, 0);
    private static final boolean PAID = false;
    private static final boolean NOT_SHOW = false;

    public static final String CUSTOMER_CUI = "1234567-8";
    public static final String CUSTOMER_FULL_NAME = "Juan Pérez";
    public static final String PACKAGE_ID = "PKG001";

    public static final CreatePlayerRequestDTO PLAYER_1 = new CreatePlayerRequestDTO("Player 1", Integer.valueOf(25));
    public static final CreatePlayerRequestDTO PLAYER_2 = new CreatePlayerRequestDTO("Player 2", Integer.valueOf(28));
    private static final GameResponseDTO GAME_RESPONSE_DTO = new GameResponseDTO("game-789", "reserv", List.of(),
            false);
    public static final List<CreatePlayerRequestDTO> VALID_PLAYERS = List.of(PLAYER_1, PLAYER_2);

    private CreateReservationRequestDTO createDTO;
    private Reservation reservation;

    @BeforeEach
    void setUp() {

        createDTO = new CreateReservationRequestDTO(START_TIME, END_TIME, DATE,
                CUSTOMER_CUI, CUSTOMER_FULL_NAME, PACKAGE_ID, VALID_PLAYERS);

        reservation = new Reservation(createDTO);
        reservation.setId(RESERVATION_ID);
        reservation.setPaid(PAID);
        reservation.setNotShow(NOT_SHOW);
    }

    /**
     * dado: los datos de reserva son válidos y no existe duplicado.
     * cuando: se llama a createPresentialReservation.
     * entonces: se guarda la reserva, se crea el juego y se genera el QR.
     */
    @Test
    void createPresentialReservationReturnsQrBytes() throws Exception {
        // arrange
        when(reservationRepository.existsByStartTimeAndEndTimeAndDate(any(), any(), any())).thenReturn(false);
        when(reservationRepository.save(any())).thenReturn(reservation);
        when(gameClientPort.createGame(any())).thenReturn(GAME_RESPONSE_DTO);
        when(qrCodeAdapter.generateQrCode(any())).thenReturn("fake-qr".getBytes());

        // act
        byte[] qrBytes = reservationService.createPresentialReservation(createDTO);

        // assert
        assertAll(
                () -> assertNotNull(qrBytes),
                () -> assertTrue(qrBytes.length > 0));
    }

    /**
     * dado: los datos de reserva son válidos y no existe duplicado.
     * cuando: se llama a createOnlineReservation.
     * entonces: se guarda la reserva y se asocia el ID del juego.
     */
    @Test
    void createOnlineReservationReturnsReservationWithGameId() throws Exception {
        // arrange
        when(reservationRepository.existsByStartTimeAndEndTimeAndDate(START_TIME, END_TIME, DATE)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(gameClientPort.createGame(any())).thenReturn(GAME_RESPONSE_DTO);

        // act
        Reservation result = reservationService.createOnlineReservation(createDTO);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("game-789", result.getGameId()),
                () -> verify(reservationRepository).save(any(Reservation.class)),
                () -> verify(gameClientPort).createGame(any(CreateGameRequestDTO.class)));
    }

    /**
     * dado: la reserva existe, no ha sido cancelada ni pagada.
     * cuando: se llama a payReservation.
     * entonces: se marca como pagada y se genera el QR.
     */
    @Test
    void payReservationMarksAsPaidAndReturnsQr() throws Exception {
        // arrange
        reservation.setGameId("game-789");
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(qrCodeAdapter.generateQrCode(any())).thenReturn("fake-qr".getBytes());

        // act
        byte[] qrBytes = reservationService.payReservation(RESERVATION_ID);

        // assert
        assertAll(
                () -> assertNotNull(qrBytes),
                () -> assertTrue(reservation.getPaid()));
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

        assertTrue(result.getNotShow());
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
        reservation.setNotShow(true);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.cancelReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: no existe una reserva con el ID proporcionado.
     * cuando: se llama a setPaymentReservation.
     * entonces: se lanza NotFoundException.
     */
    @Test
    void shouldThrowNotFoundWhenSettingPaymentOnNonexistentReservation() {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.payReservation(RESERVATION_ID));

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

        assertThrows(IllegalStateException.class, () -> reservationService.payReservation(RESERVATION_ID));

        verify(reservationRepository, never()).save(any());
    }

    /**
     * dado: la reserva ha sido cancelada.
     * cuando: se llama a setPaymentReservation.
     * entonces: se lanza IllegalStateException.
     */
    @Test
    void shouldThrowWhenReservationCancelledOnSetPayment() {
        reservation.setNotShow(true);
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationService.payReservation(RESERVATION_ID));

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
                null, START_TIME, END_TIME, LocalDate.now(), true, false, false);

        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAll(any(Specification.class))).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservations(dto);

        assertEquals(1, result.size());
        assertEquals(START_TIME, result.get(0).getStartTime());
        verify(reservationRepository).findAll(any(Specification.class));
    }

    /**
     * dado: DTO con fechas válidas
     * cuando: se llama getReservationsBetweenDates
     * entonces: retorna lista con las reservas en ese rango
     */
    @Test
    void getReservationsBetweenDatesReturnsMatchingReservations() {
        // arrange
        LocalDate startDate = LocalDate.of(2025, 5, 10);
        LocalDate endDate = LocalDate.of(2025, 5, 12);
        PeriodRequestDTO dto = new PeriodRequestDTO(startDate, endDate);

        when(reservationRepository.findReservationByDateBetween(any(), any()))
                .thenReturn(List.of(reservation));

        // act
        List<Reservation> result = reservationService.getReservationsBetweenDates(dto);

        // assert
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> verify(reservationRepository, times(1))
                        .findReservationByDateBetween(any(), any()));
    }

    /**
     * dado: fechas válidas con reservas populares agrupadas por hora
     * cuando: se llama getPopularHoursBetweenDates
     * entonces: retorna lista de estadísticas con la cantidad por franja
     */
    @Test
    void getPopularHoursBetweenDatesReturnsStatsCorrectly() {
        // arrange
        LocalDate startDate = LocalDate.of(2025, 5, 10);
        LocalDate endDate = LocalDate.of(2025, 5, 12);
        PeriodRequestDTO dto = new PeriodRequestDTO(startDate, endDate);

        ReservationTimeStatsDTO stat1 = new ReservationTimeStatsDTO(
                START_TIME, END_TIME, 3l);
        ReservationTimeStatsDTO stat2 = new ReservationTimeStatsDTO(
                END_TIME, END_TIME.plusHours(1), 5l);

        when(reservationRepository.findReservationsGroupedByTimeRangeAndFilteredByDate(
                any(), any()))
                .thenReturn(List.of(stat1, stat2));

        // act
        List<ReservationTimeStatsDTO> result = reservationService.getPopularHoursBetweenDates(dto);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals(3, result.get(0).getTotal()),
                () -> assertEquals(5, result.get(1).getTotal()),
                () -> verify(reservationRepository, times(1))
                        .findReservationsGroupedByTimeRangeAndFilteredByDate(any(), any()));
    }

}
