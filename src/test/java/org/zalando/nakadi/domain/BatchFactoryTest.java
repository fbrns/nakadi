package org.zalando.nakadi.domain;

import org.json.JSONException;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class BatchFactoryTest {

    @Test
    public void testOneEvent() {
        final String events = "[{\"name\":\"MyEvent\"}]";
        final List<BatchItem> batch = BatchFactory.from(events);
        assertEquals(1, batch.size());
        assertEquals(18, batch.get(0).getEventSize());
        assertEquals("{\"name\":\"MyEvent\"}", batch.get(0).getEvent().toString());
    }

    @Test
    public void testMultipleEvents() {
        final String events = "[{\"name\":\"MyEvent\"},{\"name\":\"MyOtherEvent\"}]";
        final List<BatchItem> batch = BatchFactory.from(events);
        assertEquals(2, batch.size());
        assertEquals(18, batch.get(0).getEventSize());
        assertEquals(23, batch.get(1).getEventSize());
        assertEquals("{\"name\":\"MyEvent\"}", batch.get(0).getEvent().toString());
        assertEquals("{\"name\":\"MyOtherEvent\"}", batch.get(1).getEvent().toString());
    }

    @Test
    public void testNestedArrays() {
        final String events = "[{\"name\":\"MyEvent\", \"array\":[{\"developer\": \"Ricardo\"}," +
                "{\"developer\": \"Sergii\"},{\"field\":[\"hello\",\"world\"]}]},{\"name\":\"MyOtherEvent\"}]";
        final List<BatchItem> batch = BatchFactory.from(events);
        assertEquals(2, batch.size());
        assertEquals("{\"array\":[{\"developer\":\"Ricardo\"},{\"developer\":\"Sergii\"}," +
                "{\"field\":[\"hello\",\"world\"]}],\"name\":\"MyEvent\"}",
                batch.get(0).getEvent().toString());
        assertEquals("{\"name\":\"MyOtherEvent\"}", batch.get(1).getEvent().toString());
    }

    @Test
    public void testMalformedJSON() {
        final String events = "[{\"hello\":\"world\",]";
        try {
            BatchFactory.from(events);
            fail();
        } catch (JSONException e) {}
    }
}