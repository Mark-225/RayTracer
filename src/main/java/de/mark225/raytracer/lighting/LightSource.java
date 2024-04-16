package de.mark225.raytracer.lighting;

import org.spongepowered.math.vector.Vector3d;

import java.awt.*;

public record LightSource(Vector3d worldPosition, Color specularColor, Color diffuseColor, Color ambientColor) {
}
