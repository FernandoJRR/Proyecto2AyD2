package com.ayd.reservation_service.reservation.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;
import com.ayd.game_service_common.players.dtos.CreatePlayerRequestDTO;
import com.ayd.reservation_service.reservation.dtos.CreateReservationDTO;
import com.ayd.reservation_service.reservation.dtos.CreateReservationOnlineRequestDTO;
import com.ayd.reservation_service.reservation.dtos.CreateReservationPresentialRequestDTO;
import com.ayd.reservation_service.reservation.dtos.PayReservationRequestDTO;
import com.ayd.reservation_service.reservation.mappers.ReservationMapper;
import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.reservation_service.reservation.ports.ForGameClientPort;
import com.ayd.reservation_service.reservation.ports.ForInvoiceClientPort;
import com.ayd.reservation_service.reservation.ports.ForReportClientPort;
import com.ayd.reservation_service.reservation.ports.ForReservationPort;
import com.ayd.reservation_service.reservation.repositories.ReservationRepository;
import com.ayd.reservation_service.reservation.specifications.ReservationSpecification;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;
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
    private final ForInvoiceClientPort forInvoiceClient;
    private final ForReportClientPort forReportClientPort;
    private final ReservationMapper reservationMapper;


    @Override
    public byte[] createPresentialReservation(CreateReservationPresentialRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException, WriterException, IOException {
        // mandamos a crear la reserva
        Reservation reservation = createReservation(createReservationRequestDTO, List.of());
        // pagar la reservacion que genera un pdf
        byte[] pdf = payReservation(reservation, createReservationRequestDTO.getCreateInvoiceRequestDTO());

        return pdf;
        // ahora mandamos a crear el qr con el id de la reserva y id del juego
        // return createReservationQR(reservation);
    }

    @Override
    public byte[] createOnlineReservation(CreateReservationOnlineRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException {
        // mandamos a crear la reserva
        Reservation reservation = createReservation(createReservationRequestDTO,
                createReservationRequestDTO.getPlayers());
        ReservationResponseDTO reservationResponseDTO = reservationMapper
                .fromReservationToReservationResponseDTO(reservation);
        // CREAR UN PDF UNICAMENTE CON LA INFORMACION DE LA RESERVA
        byte[] pdf = forReportClientPort
                .exportReservationTicket(reservationResponseDTO);
        return pdf;
    }

    private Reservation createReservation(CreateReservationDTO createReservationRequestDTO,
            List<CreatePlayerRequestDTO> players)
            throws DuplicatedEntryException {
        if (reservationRepository.existsByDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                createReservationRequestDTO.getDate(),
                createReservationRequestDTO.getStartTime(), createReservationRequestDTO.getEndTime())) {
            throw new DuplicatedEntryException("Ya existe una reserva en el mismo horario.");
        }

        // creamos la reserva con los datos de la peticion
        Reservation reservation = new Reservation(createReservationRequestDTO);
        Reservation savedReservation = reservationRepository.save(reservation);

        // creamos el objeto para enviar al servicio de juegos
        CreateGameRequestDTO createGameRequestDTO = new CreateGameRequestDTO(savedReservation.getId(),
                players);

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
    public byte[] payReservation(PayReservationRequestDTO payReservationRequestDTO)
            throws NotFoundException, WriterException, IOException {
        Reservation reservation = getReservation(payReservationRequestDTO.getReservationId());

        if (reservation.getNotShow()) {
            throw new IllegalStateException("La reserva ha sido cancelada.");
        }
        if (reservation.getPaid()) {
            throw new IllegalStateException("La reserva ya ha sido pagada.");
        }

        byte[] pdf = payReservation(reservation, payReservationRequestDTO.getCreateInvoiceRequestDTO());

        return pdf;
        // creamos el qr
        // return createReservationQR(reservation);
    }

    byte[] payReservation(Reservation reservation, CreateInvoiceRequestDTO createReservationRequestDTO) {

        // MANR A GUARDAR LA FACTURA CreateReservationRequestDTO.createInvoiceRequestDTO
        InvoiceResponseDTO invoice = forInvoiceClient
                .createInvoice(createReservationRequestDTO);

        // MARCAR COMO PAGAD LA RESERVACION
        reservation.setPaid(true);
        reservation.setInvoiceId(invoice.getId());

        // UNA VEZ OBTENIDA LA RESPUESTA CON LA FACTURA CREADA SE RECOMIENDA CRRAR UN
        // PDF CON EL QR Y LA INFO DE LA FACTURA
        // SINO SOLO DEVOLVER EL QR ASI COMO ESTA
        ReservationResponseDTO reservationResponseDTO = reservationMapper
                .fromReservationToReservationResponseDTO(reservation);

        byte[] pdf = forReportClientPort
                .exportInvoiceWithQR(new ReservationInterServiceDTO(reservationResponseDTO, invoice));

        return pdf;
    }

    @Override
    public boolean deleteReservation(String reservationId) throws IllegalStateException, NotFoundException {
        Reservation reservation = getReservation(reservationId);
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
    public byte[] getReservationInvoice(String reservationId)
            throws WriterException, IOException, NotFoundException {
        // mandamos a traer la reserva
        Reservation reservation = getReservation(reservationId);
        // si la reserva no ha sido pagada entonces no se puede cargar el qr
        if (!reservation.getPaid() || reservation.getInvoiceId().isBlank()) {
            throw new IllegalStateException("La reservacion no ha sido pagada");
        }

        // sino entonces podemos mandar a traer los datos de la factura
        InvoiceResponseDTO invoice = forInvoiceClient.getInvoice(reservation.getInvoiceId());

        // mandamos a crear la invoice con el qr
        ReservationResponseDTO reservationResponseDTO = reservationMapper
                .fromReservationToReservationResponseDTO(reservation);
        byte[] pdf = forReportClientPort
                .exportInvoiceWithQR(new ReservationInterServiceDTO(reservationResponseDTO, invoice));

        return pdf;
        // ahora mandamos a crear el qr con el id de la reserva y id del juego
        // return createReservationQR(reservation);
    }


}
