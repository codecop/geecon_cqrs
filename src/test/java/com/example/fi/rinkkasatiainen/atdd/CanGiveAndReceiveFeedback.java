package com.example.fi.rinkkasatiainen.atdd;


import com.example.fi.rinkkasatiainen.Stars;
import com.example.fi.rinkkasatiainen.web.commands.*;
import com.example.fi.rinkkasatiainen.web.queries.SessionFeedbackResult;
import com.example.fi.rinkkasatiainen.web.queries.SessionFeedbackRoute;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CanGiveAndReceiveFeedback {

    @Test
    public void can_create_user_and_give_feedback_to_session() throws Exception {
        SessionUUID session = given_a_session_exists_with_name();
        ParticipantUUID participant = and_I_have_registered_as_participant();

        when_I(participant).rates_session(session).as( Stars.FIVE );

        then_session(session).should_have_average_rating_of( 5.0 );
    }

    private void then_session_should_have_average_rating_of(int expectedRating) {
        assertThat(true, equalTo(false));
    }

    private SessionResponse then_session(SessionUUID session) {
        return ( rating ) -> {
            ResponseEntity<SessionFeedbackResult> feedbackResult = new SessionFeedbackRoute().getSession(session.uuid.toString());
            SessionFeedbackResult body = feedbackResult.getBody();
            assertThat(body.averageRating, equalTo(rating));
        };
    }

    private RateSession when_I(ParticipantUUID participant) {
        RateSession rateSession = sessionUUID -> stars -> {
            new SessionRoute().register( sessionUUID.uuid.toString() , new Participant(participant.uuid));
            new SessionRoute().rate( sessionUUID.uuid.toString() , new SessionFeedback(stars.ordinal()));
        };
        return rateSession;
    }


    private ParticipantUUID and_I_have_registered_as_participant() {
        ResponseEntity<Void> responseEntity = new ParticipantsRoute().create();
        String uuid = getUUIDFromLocationHeader(responseEntity);
        return new ParticipantUUID(UUID.fromString(uuid));
    }


    private SessionUUID given_a_session_exists_with_name() {
        ResponseEntity<Void> sessionResponseEntity= new SessionsRoute().create();
        String uuid = getUUIDFromLocationHeader(sessionResponseEntity);
        return new SessionUUID(UUID.fromString(uuid));
    }

    private String getUUIDFromLocationHeader(ResponseEntity<Void> sessionResponseEntity) {
        return sessionResponseEntity.getHeaders().get("location")
                .stream().map( str -> str.replaceAll(".*/", "") ).findFirst().get();
    }

    private class ParticipantUUID {
        public final UUID uuid;

        public ParticipantUUID(UUID uuid) {
            this.uuid = uuid;
        }
    }

}
