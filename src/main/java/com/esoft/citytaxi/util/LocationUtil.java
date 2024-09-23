package com.esoft.citytaxi.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class LocationUtil {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point mapToPoint(final double longitude, final double latitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        point.setSRID(4326);
        return point;
    }
}
