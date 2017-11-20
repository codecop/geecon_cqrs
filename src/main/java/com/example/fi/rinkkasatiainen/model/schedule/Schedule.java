package com.example.fi.rinkkasatiainen.model.schedule;

import com.example.fi.rinkkasatiainen.model.Event;
import com.example.fi.rinkkasatiainen.model.EventStore;
import com.example.fi.rinkkasatiainen.model.SessionUUID;
import com.example.fi.rinkkasatiainen.model.session.Session;
import com.example.fi.rinkkasatiainen.model.session.SessionDetails;
import com.example.fi.rinkkasatiainen.model.session.events.SessionCreated;
import com.example.fi.rinkkasatiainen.model.session.projections.SessionFeedbackResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Schedule {
    private final Supplier<SessionUUID> supplier;
    private final EventStore eventStore;

    public Schedule(Supplier<SessionUUID> supplier, EventStore eventStore) {
        this.supplier = supplier;
        this.eventStore = eventStore;
    }

    public SessionUUID newSessionUUID() {
        return supplier.get();
    }

    public Session findSession(SessionUUID uuid) {
        List<Event> events = eventStore.findByUuid(uuid.getId());
        return Session.load(events);
    }

    public SessionFeedbackResult findSessionFeeback(SessionUUID uuid) {
        List<Event> events = eventStore.findByUuid(uuid.getId());
        return SessionFeedbackResult.load(events);
    }

    public List<SessionDetails> findAllSessions() {
        List<SessionCreated> allSessionCreatedEvents = eventStore.findAllByType(SessionCreated.class);
        List<SessionUUID> allUUIDs = allSessionCreatedEvents.stream().map(e -> e.uuid).collect(Collectors.toList());

        Map<SessionUUID, List<Event>> all = eventStore.findAll(allUUIDs);
        // This would be a perfect place to build a reactive stream.
        return all.entrySet().stream().map( entry -> SessionDetails.load(entry.getValue())).collect(Collectors.toList());
    }
}
