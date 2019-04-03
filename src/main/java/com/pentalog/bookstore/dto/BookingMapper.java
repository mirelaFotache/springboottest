package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Booking;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface BookingMapper {

    BookingDTO toBookingDTO(Booking booking);

    Collection<BookingDTO> toBookingDTOs(Collection<Booking> bookings);

    Booking toBooking(BookingDTO bookingDTO);
}
