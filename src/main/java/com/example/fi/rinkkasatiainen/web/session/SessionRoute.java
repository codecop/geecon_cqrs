package com.example.fi.rinkkasatiainen.web.session;

import com.example.fi.rinkkasatiainen.model.session.commands.RegisterParticipantCommand;
import com.example.fi.rinkkasatiainen.model.session.commands.RegisterParticipantCommandHandler;
import com.example.fi.rinkkasatiainen.web.participants.Participant;
import com.example.fi.rinkkasatiainen.web.session.commands.SessionFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(SessionRoute.V1_SESSION)
public class SessionRoute {
    public static final String V1_SESSION = "/v1/session/{sessionId}";

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final RegisterParticipantCommandHandler registerParticipantCommandHandler;

    public SessionRoute(RegisterParticipantCommandHandler registerParticipantCommandHandler) {
        this.registerParticipantCommandHandler = registerParticipantCommandHandler;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Void> register(@PathVariable(value = "sessionId") String sessionId, @RequestBody Participant participant) {
        registerParticipantCommandHandler.handles( new RegisterParticipantCommand(participant.uuid, UUID.fromString(sessionId)));

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> rate(@PathVariable(value = "sessionId") String sessionId, @RequestBody SessionFeedback participant) {
        return ResponseEntity.ok().build();
    }
}
