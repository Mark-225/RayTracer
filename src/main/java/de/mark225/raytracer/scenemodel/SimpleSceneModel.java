package de.mark225.raytracer.scenemodel;

import de.mark225.raytracer.objects.SceneObject;
import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;

import java.util.List;

public class SimpleSceneModel implements SceneModel{
    private List<SceneObject> objectList;

    public SimpleSceneModel(List<SceneObject> objectList) {
        this.objectList = objectList;
    }

    @Override
    public Intersection firstIntersection(Vector3d origin, Vector3d direction, boolean simple) {
        double minDistance = Double.MAX_VALUE;
        Intersection minIntersection = null;
        for(SceneObject object : objectList){
            Intersection intersection = object.getIntersection(origin, direction);
            if(intersection != null && intersection.distance() < minDistance && intersection.distance() > 0) {
                minIntersection = intersection;
                minDistance = intersection.distance();
            }
        }
        return minIntersection;
    }
}
