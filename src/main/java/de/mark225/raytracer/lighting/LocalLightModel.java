package de.mark225.raytracer.lighting;

import de.mark225.raytracer.Raytracer;
import de.mark225.raytracer.objects.MaterialProperties;
import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;

import java.awt.*;
import java.util.stream.Stream;

public interface LocalLightModel {

    public Color calculateColor (Intersection intersection, Raytracer raytracer);

}
