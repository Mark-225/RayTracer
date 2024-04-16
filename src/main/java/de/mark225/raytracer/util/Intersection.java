package de.mark225.raytracer.util;

import de.mark225.raytracer.objects.SceneObject;
import org.spongepowered.math.vector.Vector2d;
import org.spongepowered.math.vector.Vector3d;

public record Intersection(Vector3d origin, double distance, Vector3d location, Vector3d incoming, Vector3d normal, Vector3d reflection, Vector3d refraction, SceneObject object) {

    public static Intersection of(Vector3d origin, double distance, Vector3d incoming, Vector3d normal, SceneObject object){
        Vector3d reflection = getReflectionVector(incoming, normal);
        Vector3d refraction = getReflectionVector(incoming, reflection.mul(-1));
        return new Intersection(origin, distance, origin.add(incoming.mul(distance)), incoming, normal, reflection, refraction, object);
    }

    public static Vector3d getReflectionVector(Vector3d incoming, Vector3d normal){
        return incoming.sub(normal.mul( 2 * incoming.dot(normal)));
    }

}
