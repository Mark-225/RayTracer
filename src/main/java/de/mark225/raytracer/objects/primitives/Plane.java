package de.mark225.raytracer.objects.primitives;

import de.mark225.raytracer.objects.MaterialProperties;
import de.mark225.raytracer.objects.SceneObject;
import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;

import java.util.Arrays;

public class Plane extends SceneObject {

    Vector3d normal;
    double distance;

    public Plane(Vector3d normal, double distance, MaterialProperties materialProperties) {
        super(Vector3d.ZERO, materialProperties);
        this.normal = normal;
        this.distance = distance;
    }

    @Override
    public Intersection getIntersection(Vector3d origin, Vector3d direction) {
        double b = Arrays.stream(normal.mul(direction).toArray()).sum();
        if(Math.abs(b) < 0.0005) return null;
        double a = -1 * (Arrays.stream(normal.mul(origin).toArray()).sum() + distance);
        double t = a/b;
        if(t <= 0.0005) return null;
        return Intersection.of(origin, t, direction, normal, this);
    }
}
