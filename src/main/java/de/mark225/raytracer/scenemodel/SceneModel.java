package de.mark225.raytracer.scenemodel;

import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;

public interface SceneModel {
    Intersection firstIntersection(Vector3d origin, Vector3d direction, boolean simple);
    default Intersection firstIntersection(Vector3d origin, Vector3d direction) {
        return firstIntersection(origin, direction, false);
    }
}
