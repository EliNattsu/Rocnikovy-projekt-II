package cz.catparadise.service;

import cz.catparadise.model.Cat;
import cz.catparadise.model.Reservation;
import cz.catparadise.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service třída pro správu rezervací.
 * Obsahuje business logiku pro vytváření, úpravu a mazání rezervací.
 */
@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Uloží novou rezervaci do databáze.
     * Před uložením zkontroluje dostupnost pokoje v zadaném termínu
     * a vygeneruje unikátní referenční číslo.
     * @param reservation objekt rezervace k uložení
     * @return uložená rezervace s přiřazeným ID a referenčním číslem
     * @throws IllegalArgumentException pokud je pokoj již obsazen v daném termínu
     */
    public Reservation saveReservation(Reservation reservation) {
        logger.info("Vytváření nové rezervace pro uživatele ID: {}",
                reservation.getUser().getUserId());

        // Kontrola dostupnosti pokoje v zadaném termínu
        if (reservation.getRoom() != null) {
            List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                    reservation.getRoom().getRoomId(),
                    reservation.getStartDate(),
                    reservation.getEndDate()
            );
            if (!overlapping.isEmpty()) {
                logger.warn("Pokoj ID: {} není dostupný v termínu {} - {}",
                        reservation.getRoom().getRoomId(),
                        reservation.getStartDate(),
                        reservation.getEndDate());
                throw new IllegalArgumentException("Pokoj není dostupný v zadaném termínu.");
            }
        }

        // Generování unikátního referenčního čísla ve formátu RES-XXXXXXXX
        reservation.setReferenceNumber("RES-" + UUID.randomUUID().toString().substring(0, 8));
        reservation.setStatus("PENDING");
        Reservation saved = reservationRepository.save(reservation);
        logger.info("Rezervace úspěšně vytvořena s číslem: {}", saved.getReferenceNumber());
        return saved;
    }

    /**
     * Vrátí seznam všech rezervací v systému.
     * Dostupné pouze pro administrátory.
     * @return seznam všech rezervací
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Najde rezervaci podle ID.
     * @param id ID rezervace
     * @return Optional obsahující rezervaci nebo prázdný Optional
     */
    public Optional<Reservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    /**
     * Smaže rezervaci podle ID.
     * @param id ID rezervace ke smazání
     */
    public void deleteReservation(Integer id) {
        logger.info("Mazání rezervace ID: {}", id);
        reservationRepository.deleteById(id);
    }

    /**
     * Vrátí seznam rezervací podle statusu.
     * @param status status rezervace (PENDING, CONFIRMED, CANCELLED, COMPLETED)
     * @return seznam rezervací se zadaným statusem
     */
    public List<Reservation> getReservationsByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    /**
     * Vrátí seznam rezervací konkrétního uživatele.
     * @param userId ID uživatele
     * @return seznam rezervací daného uživatele
     */
    public List<Reservation> getReservationsByUser(Integer userId) {
        return reservationRepository.findByUserUserId(userId);
    }

    /**
     * Změní status rezervace.
     * @param id ID rezervace
     * @param newStatus nový status rezervace
     * @return upravená rezervace
     * @throws RuntimeException pokud rezervace s daným ID neexistuje
     */
    public Reservation updateReservationStatus(Integer id, String newStatus) {
        logger.info("Změna statusu rezervace ID: {} na {}", id, newStatus);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Rezervace ID: {} nenalezena", id);
                    return new RuntimeException("Rezervace nenalezena: " + id);
                });
        reservation.setStatus(newStatus);
        return reservationRepository.save(reservation);
    }

    /**
     * Přidá kočku k existující rezervaci.
     * @param reservationId ID rezervace
     * @param catId ID kočky
     * @param catService service pro práci s kočkami
     * @return upravená rezervace s přidanou kočkou
     */
    public Reservation addCatToReservation(Integer reservationId, Integer catId, CatService catService) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Rezervace nenalezena: " + reservationId));

        Cat cat = catService.getCatById(catId)
                .orElseThrow(() -> new RuntimeException("Kočka nenalezena: " + catId));

        reservation.getCats().add(cat);
        return reservationRepository.save(reservation);
    }
    /**
     * Vratí strankovaný seznam rezervaci konkrétního uživatele
     * @param userId ID uživatele
     * @param pageable objekt obsahujicí číslo stránk a velikost stránky
     * @return stránka rezervací daného uživatele
     */
    public Page<Reservation> getReservationsByUserPaged(Integer userId, Pageable pageable) {
        logger.info("Výpis rezervací uživatele ID: {} se stránkováním", userId);
        return reservationRepository.findByUserUserId(userId, pageable);
    }
}