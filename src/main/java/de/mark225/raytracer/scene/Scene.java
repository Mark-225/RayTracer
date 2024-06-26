package de.mark225.raytracer.scene;

import de.mark225.raytracer.lighting.LightSource;
import de.mark225.raytracer.lighting.LocalLightModel;
import de.mark225.raytracer.objects.SceneObject;

import java.util.Collection;
import java.util.List;

public record Scene(Camera camera, Collection<LightSource> lightSources, LocalLightModel lightModel, SceneObject[] objects) {}
