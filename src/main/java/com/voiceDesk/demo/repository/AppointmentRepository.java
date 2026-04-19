package com.voiceDesk.demo.repository;

import com.voiceDesk.demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}