//  Copyright 2020-2021 Herald Project Contributors
//  SPDX-License-Identifier: Apache-2.0
//

package io.heraldprox.herald.sensor.datatype;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SuppressWarnings("ConstantConditions")
public class LocationsTests {

    @Test
    public void testInitNull() {
        assertNull(new Locations(null, null, null).value);
        assertNull(new Locations(null, null, null).start);
        assertNull(new Locations(null, null, null).end);
        assertNotNull(new Locations(null, null, null).description());
    }

    @Test
    public void testInit() {
        final LocationReference locationReference = new PlacenameLocationReference("place");
        assertEquals(locationReference, new Locations(locationReference, new Date(1), new Date(2)).value);
        assertEquals(1, new Locations(locationReference, new Date(1), new Date(2)).start.getTime());
        assertEquals(2, new Locations(locationReference, new Date(1), new Date(2)).end.getTime());
        assertNotNull(new Locations(locationReference, new Date(1), new Date(2)).description());
    }
}
