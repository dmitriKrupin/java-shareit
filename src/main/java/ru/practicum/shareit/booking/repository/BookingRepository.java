package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdAndStatusInOrderByEndDesc(Long bookerId, List<Status> statuses);

    List<Booking> findAllByBooker_IdOrderByEndDesc(Long bookerId);

    List<Booking> findAllByItem_IdInOrderByEndDesc(List<Long> ownerIds);

    List<Booking> findAllByItem_IdInAndStatusInOrderByEndDesc(List<Long> ownerIds, List<Status> statuses);
}
