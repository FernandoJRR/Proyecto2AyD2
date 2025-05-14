package com.ayd.reservation_service.reservation.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;
import com.ayd.reservation_service.qr.services.QrCodeAdapter;
import com.ayd.reservation_service.reservation.dtos.CreateReservationRequestDTO;
import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.reservation_service.reservation.ports.ForGameClientPort;
import com.ayd.reservation_service.reservation.ports.ForReservationPort;
import com.ayd.reservation_service.reservation.repositories.ReservationRepository;
import com.ayd.reservation_service.reservation.specifications.ReservationSpecification;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.shared.security.AppProperties;
import com.ayd.sharedReservationService.dto.ReservationSpecificationRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;
import com.google.zxing.WriterException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class ReservationService implements ForReservationPort {

    private final ReservationRepository reservationRepository;
    private final ForGameClientPort gameClientPort;
    private final QrCodeAdapter qrCodeAdapter;
    private final AppProperties appProperties;

    @Override
    public byte[] createPresentialReservation(CreateReservationRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException, WriterException, IOException {
        // mandamos a crear la reserva
        Reservation reservation = createReservation(createReservationRequestDTO);
        // ahora mandamos a crear el qr con el id de la reserva y id del juego
        return createReservationQR(reservation);
    }

    @Override
    public Reservation createOnlineReservation(CreateReservationRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException {
        // mandamos a crear la reserva
        Reservation reservation = createReservation(createReservationRequestDTO);
        // ahora mandamos a crear el qr con el id de la reserva y id del juego
        return reservation;
    }

    private Reservation createReservation(CreateReservationRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException {
        if (reservationRepository.existsByStartTimeAndEndTimeAndDate(
                createReservationRequestDTO.getStartTime(), createReservationRequestDTO.getEndTime(),
                createReservationRequestDTO.getDate())) {
            throw new DuplicatedEntryException("Ya existe una reserva en el mismo horario.");
        }

        // creamos la reserva con los datos de la peticion
        Reservation reservation = new Reservation(createReservationRequestDTO);
        Reservation savedReservation = reservationRepository.save(reservation);

        // creamos el objeto para enviar al servicio de juegos
        CreateGameRequestDTO createGameRequestDTO = new CreateGameRequestDTO(savedReservation.getId(),
                createReservationRequestDTO.getPlayers());

        // mandamos a crear el juego con los datos de los jugadores
        GameResponseDTO savedGame = gameClientPort.createGame(createGameRequestDTO);

        // refrescamos la reservacion para guardar el id del juego
        savedReservation.setGameId(savedGame.getId());

        // retornamos la reservacion creada para que el metodo crear lo maneje
        return savedReservation;
    }

    @Override
    public Reservation cancelReservation(String reservationId) throws IllegalStateException, NotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("No se encontró la reserva con el ID: " + reservationId));
        if (reservation.getPaid()) {
            throw new IllegalStateException("La reserva ya ha sido pagada.");
        }
        if (reservation.getNotShow()) {
            throw new IllegalStateException("La reserva ya ha sido cancelada.");
        }
        reservation.setNotShow(true);
        return reservationRepository.save(reservation);
    }

    @Override
    public byte[] payReservation(String reservationId) throws  NotFoundException, WriterException, IOException {
        Reservation reservation = getReservation(reservationId);

        if (reservation.getNotShow()) {
            throw new IllegalStateException("La reserva ha sido cancelada.");
        }
        if (reservation.getPaid()) {
            throw new IllegalStateException("La reserva ya ha sido pagada.");
        }

        //mandamos a pagar toda la reserva al invoice service

        reservation.setPaid(true);

        //creamos el qr
        return createReservationQR(reservation);
    }

    @Override
    public boolean deleteReservation(String reservationId) throws IllegalStateException, NotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("No se encontró la reserva con el ID: " + reservationId));
        if (reservation.getPaid()) {
            throw new IllegalStateException("La reserva ya ha sido pagada.");
        }
        reservationRepository.delete(reservation);
        return true;
    }

    @Override
    public List<Reservation> getReservations(ReservationSpecificationRequestDTO reservationSpecificationRequestDTO) {
        if (reservationSpecificationRequestDTO == null) {
            return reservationRepository.findAll();
        }
        Specification<Reservation> spec = Specification
                .where(ReservationSpecification.hasStartTime(reservationSpecificationRequestDTO.getStartTime()))
                .and(ReservationSpecification.hasEndTime(reservationSpecificationRequestDTO.getEndTime()))
                .and(ReservationSpecification.hasDate(reservationSpecificationRequestDTO.getDate()))
                .and(ReservationSpecification.hasUserId(reservationSpecificationRequestDTO.getUserId()))
                .and(ReservationSpecification.isOnline(reservationSpecificationRequestDTO.getOnline()))
                .and(ReservationSpecification.isPaid(reservationSpecificationRequestDTO.getPaid()))
                .and(ReservationSpecification.isCancelled(reservationSpecificationRequestDTO.getCancelled()));
        return reservationRepository.findAll(spec);
    }

    /**
     * Obtiene todas las reservas que se encuentran dentro del rango de fechas
     * especificado.
     *
     * @param periodRequestDTO objeto que contiene la fecha de inicio y la fecha de
     *                         fin para el filtro.
     * @return una lista de objetos {@link Reservation} que tienen una fecha dentro
     *         del rango proporcionado.
     */
    @Override
    public List<Reservation> getReservationsBetweenDates(PeriodRequestDTO periodRequestDTO) {
        return reservationRepository.findReservationByDateBetween(periodRequestDTO.getStartDate(),
                periodRequestDTO.getEndDate());
    }

    /**
     * Obtiene una lista de rangos horarios con la cantidad de reservas realizadas
     * en cada uno,
     * dentro del rango de fechas especificado.
     *
     * @param periodRequestDTO objeto que contiene la fecha de inicio y la fecha de
     *                         fin para el filtro.
     * @return una lista de {@link ReservationTimeStatsDTO} que representan los
     *         rangos horarios más populares.
     */
    @Override
    public List<ReservationTimeStatsDTO> getPopularHoursBetweenDates(PeriodRequestDTO periodRequestDTO) {
        return reservationRepository.findReservationsGroupedByTimeRangeAndFilteredByDate(
                periodRequestDTO.getStartDate(), periodRequestDTO.getEndDate());
    }

    @Override
    public Reservation getReservation(String reservationId) throws NotFoundException {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("No se encontró la reserva con el ID: " + reservationId));
    }

    @Override
    public byte[] getReservationQr(String reservationId)
            throws WriterException, IOException, NotFoundException {
        // mandamos a crear la reserva
        Reservation reservation = getReservation(reservationId);
        // ahora mandamos a crear el qr con el id de la reserva y id del juego
        return createReservationQR(reservation);
    }

    private byte[] createReservationQR(Reservation reservation)
            throws WriterException, IOException {
        return qrCodeAdapter
                .generateQrCode(appProperties.getFrontURL() + "/app/juegos/jugar/" + reservation.getGameId());
    }

}
