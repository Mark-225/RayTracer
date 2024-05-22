package de.mark225.raytracer.scenemodel.util;

import de.mark225.raytracer.objects.SceneObject;
import org.spongepowered.math.vector.Vector3d;

import java.util.HashSet;
import java.util.Set;

public class Octree {
    private Vector3d lowerCorner;
    private Vector3d upperCorner;
    private Vector3d center;
    private Set<SceneObject> objectsToCheck = new HashSet<>();
    private Octree[] children = new Octree[8];
}
