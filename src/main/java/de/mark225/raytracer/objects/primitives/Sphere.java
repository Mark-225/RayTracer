package de.mark225.raytracer.objects.primitives;

import de.mark225.raytracer.objects.MaterialProperties;
import de.mark225.raytracer.objects.SceneObject;
import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;

public class Sphere extends SceneObject {

    private double rsquared;

    public Sphere(Vector3d origin, double radius, MaterialProperties materialProperties) {
        super(origin, materialProperties);

        this.rsquared = radius * radius;
    }

    @Override
    public Intersection getIntersection(Vector3d origin, Vector3d direction) {
        double b = 2*(direction.x()*(origin.x() - center.x()) + direction.y()*(origin.y() - center.y()) + direction.z()*(origin.z() - center.z()));
        double dX = origin.x() - center.x();
        double dY = origin.y() - center.y();
        double dZ = origin.z() - center.z();
        double c = dX*dX + dY*dY + dZ*dZ - rsquared;
        double negativeB = b * -1;
        double det = (b*b) - (4*c);
        if (det < 0) return null;
        double root = Math.sqrt(det);
        double t0 = (negativeB - root)/2;
        double t1 = (negativeB + root)/2;
        double min = Math.min(t0, t1);
        if(min < 0.005) {
            if(t0 >= 0.005)
                min = t0;
            else if(t1 >= 0.005)
                min = t1;
            else
                return null;
        };
        Vector3d hitLocation = origin.add(direction.mul(min));
        Vector3d normal = Vector3d.from(hitLocation.x() - center.x(), hitLocation.y() - center.y(), hitLocation.z() - center.z()).normalize();
        return Intersection.of(origin, min, direction, normal, this);
    }
}
