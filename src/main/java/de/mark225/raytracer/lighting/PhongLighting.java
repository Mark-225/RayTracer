package de.mark225.raytracer.lighting;

import de.mark225.raytracer.Raytracer;
import de.mark225.raytracer.objects.MaterialProperties;
import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;


import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class PhongLighting implements LocalLightModel{
    private LightSource[] allLightSources;
    public PhongLighting(LightSource[] allLightSources) {
        this.allLightSources = allLightSources;
    }
    @Override
    public Color calculateColor(Intersection intersection, Raytracer raytracer) {
        MaterialProperties materialProperties = intersection.object().getMaterialProperties();
        List<LightSource> relevantLightSources = Stream.of(allLightSources).filter(ls -> !raytracer.isObstructed(intersection.location(), ls.worldPosition())).toList();
        float r = calculateChannel(color -> color.getRed() / 255f, intersection, materialProperties, relevantLightSources);
        float g = calculateChannel(color -> color.getGreen() / 255f, intersection, materialProperties, relevantLightSources);
        float b = calculateChannel(color -> color.getBlue() / 255f, intersection, materialProperties, relevantLightSources);
        return new Color(r, g, b);
    }

    private float calculateChannel(Function<Color, Float> channelProvider, Intersection intersection, MaterialProperties materialProperties, List<LightSource> lightSources){
        float sum = 0;
        Vector3d viewerDirection = intersection.incoming().negate();
        for (LightSource lightSource : lightSources) {
            Vector3d lightDirection = lightSource.worldPosition().sub(intersection.location());
            double dotDiffuse = lightDirection.dot(intersection.normal());
            float diffusePart = dotDiffuse < 0 ? 0f : (float) (materialProperties.diffuse() * dotDiffuse * channelProvider.apply(lightSource.diffuseColor()));
            double dotSpecular = Intersection.getReflectionVector(lightDirection.negate(), intersection.normal()).dot(viewerDirection);
            float specularPart = dotSpecular < 0 ? 0f : (float) (materialProperties.specular() * Math.pow(dotSpecular, materialProperties.shiny()) * channelProvider.apply(lightSource.specularColor()));
            sum += diffusePart + specularPart;
        }
        for (LightSource lightSource : allLightSources) {
            sum += channelProvider.apply(lightSource.ambientColor());
        }
        sum += channelProvider.apply(materialProperties.baseColor());
        return Math.clamp(sum, 0, 1);
    }
}
