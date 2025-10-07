package com.eotieno.auto.booking.repository;

import com.eotieno.auto.booking.entity.AppointmentNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentNoteRepository extends JpaRepository<AppointmentNote, Long> {
}
