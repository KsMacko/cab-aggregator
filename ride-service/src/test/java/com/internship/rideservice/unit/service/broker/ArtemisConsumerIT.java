package com.internship.rideservice.unit.service.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.commonevents.event.ChangeRideStatusEvent;
import com.internship.rideservice.service.command.CommandRideService;
import com.internship.rideservice.service.communication.ArtemisConsumer;
import com.internship.rideservice.utils.RideUtil;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtemisConsumerIT {

    @Mock
    private CommandRideService commandRideService;

    private ArtemisConsumer artemisConsumer;

    @BeforeEach
    void setUp() {
        artemisConsumer = new ArtemisConsumer(commandRideService);
    }

    @Test
    void receiveRideStatusUpdate_withValidJson_callsChangeRideStatus(){
        ChangeRideStatusEvent message = mock(ChangeRideStatusEvent.class);

        artemisConsumer.receiveRideStatusUpdate(message);

        verify(commandRideService).changeRideStatus(any(ChangeRideStatusEvent.class));
    }

    @Test
    void receiveRideStatusUpdate_withInvalidJson_throwsException() {
        assertThrows(RuntimeException.class, () -> artemisConsumer.receiveRideStatusUpdate(null));
    }
}