package de.mark225.raytracer.scene;

import org.spongepowered.math.vector.Vector3d;

public record Camera(Vector3d position, Vector3d up, Vector3d planeLoc, float hfov, float vfov) {
}
