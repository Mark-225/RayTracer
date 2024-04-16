package de.mark225.raytracer.objects;

import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;

public abstract class SceneObject {
    protected Vector3d center;
    protected MaterialProperties materialProperties;

    public SceneObject(final Vector3d center, final MaterialProperties materialProperties) {
        this.center = center;
        this.materialProperties = materialProperties;
    }

    public abstract Intersection getIntersection(Vector3d origin, Vector3d direction);

    public MaterialProperties getMaterialProperties() {
        return materialProperties;
    }
}
