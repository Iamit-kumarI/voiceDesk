package com.voiceDesk.demo.repository;

import com.voiceDesk.demo.model.AppointmentDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentDtoRepository
        extends JpaRepository<AppointmentDto, Long> {
}