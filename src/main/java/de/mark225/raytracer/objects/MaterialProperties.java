package de.mark225.raytracer.objects;

import java.awt.*;

public record MaterialProperties(float reflection, float refraction, float local, float specular, float diffuse, float shiny, Color baseColor) {
}
