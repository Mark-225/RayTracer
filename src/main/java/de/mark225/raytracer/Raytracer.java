package de.mark225.raytracer;

import de.mark225.raytracer.objects.MaterialProperties;
import de.mark225.raytracer.objects.SceneObject;
import de.mark225.raytracer.scene.Camera;
import de.mark225.raytracer.scene.Scene;
import de.mark225.raytracer.util.Intersection;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3f;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class Raytracer {

    private Scene scene;
    Vector3d renderOrigin;
    Vector3d deltaX;
    Vector3d deltaY;
    Color skyColor = Color.black;
    int resX;
    int resY;
    int maxBounces;

    public Raytracer(Scene scene, Color skyColor, int resX, int resY, int maxBounces) {
        this.scene = scene;
        this.skyColor = skyColor;
        this.resX = resX;
        this.resY = resY;
        this.maxBounces = maxBounces;
        Camera cam = scene.camera();
        Vector3d cameraToPlane = cam.planeLoc().sub(cam.position());
        double dist = cameraToPlane.length();
        Vector3d direction = cameraToPlane.normalize();
        Vector3d scrnx = direction.cross(cam.up()).normalize();
        Vector3d scrny = scrnx.cross(direction).normalize();
        deltaX = scrnx.mul(2 * dist * (Math.tan(cam.hfov()) / resX));
        deltaY = scrny.mul(2 * dist * (Math.tan(cam.vfov()) / resY));
        renderOrigin = cameraToPlane.sub(deltaX.mul(resX / 2d)).add(deltaY.mul(resY / 2d));
    }

    public BufferedImage render(){
        BufferedImage bi = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < resX; x++){
            for(int y = 0; y < resY; y++){
                Color color = traceFromScreenCoords(x, y);
                bi.setRGB(x, y, color != null ? color.getRGB() : skyColor.getRGB());
            }
        }
        return bi;
    }

    private Color traceFromScreenCoords(int x, int y){
        Vector3d origin = scene.camera().position();
        Vector3d direction = renderOrigin.sub(deltaY.mul(y)).add(deltaX.mul(x)).normalize();
        return traceOneRay(origin, direction, 0);
    }

    private Color traceOneRay(Vector3d origin, Vector3d direction, int depth){
        if(depth >= maxBounces) return null;
        Intersection intersection = firstIntersection(origin, direction);
        if(intersection == null) return skyColor;
        MaterialProperties materialProperties = intersection.object().getMaterialProperties();
        float localWeight = 0;
        Color localColor = null;
        float reflectiveWeight = 0;
        Color reflectiveColor = null;
        float refractiveWeight = 0;
        Color refractiveColor = null;
        if(materialProperties.local() > 0) {
            localColor = scene.lightModel().calculateColor(intersection, this);
            localWeight = materialProperties.local();
        }
        if(materialProperties.reflection() > 0 && (reflectiveColor = traceOneRay(intersection.location(), intersection.reflection(), depth + 1)) != null){
            reflectiveWeight = materialProperties.reflection();
        }
        if(materialProperties.refraction() > 0 && (refractiveColor = traceOneRay(intersection.location(), intersection.refraction(), depth + 1)) != null){
            refractiveWeight = materialProperties.refraction();
        }
        float totalWeight = localWeight + reflectiveWeight + refractiveWeight;
        if(totalWeight <= 0) return null;
        return vectorToColor(colorToVector(localWeight > 0 ? localColor : Color.BLACK).mul(localWeight / totalWeight)
                .add(colorToVector(reflectiveWeight > 0 ? reflectiveColor : Color.BLACK).mul(reflectiveWeight / totalWeight))
                .add(colorToVector(refractiveWeight > 0 ? refractiveColor : Color.BLACK).mul(refractiveWeight / totalWeight)));
    }

    private Vector3f colorToVector(Color color){

        return new Vector3f(Math.clamp(color.getRed() / 255f, 0, 1), Math.clamp(color.getGreen() / 255f, 0, 1), Math.clamp(color.getBlue() / 255f, 0, 1));
    }

    private Color vectorToColor(Vector3f vector){
        return new Color(vector.x(), vector.y(), vector.z());
    }

    private Intersection firstIntersection(Vector3d origin, Vector3d direction){
        double minDistance = Double.MAX_VALUE;
        Intersection minIntersection = null;
        for(SceneObject object : scene.objects()){
            Intersection intersection = object.getIntersection(origin, direction);
            if(intersection != null && intersection.distance() < minDistance && intersection.distance() > 0) {
                minIntersection = intersection;
                minDistance = intersection.distance();
            }
        }
        return minIntersection;
    }

    public boolean isObstructed(Vector3d origin, Vector3d destination){
        Vector3d delta = destination.sub(origin);
        Intersection intersection = firstIntersection(origin, delta.normalize());
        if(intersection == null) return false;
        return intersection.distance() < delta.length();
    }

    public Scene getScene() {
        return scene;
    }

    public Color getSkyColor() {
        return skyColor;
    }

    public int getResX() {
        return resX;
    }

    public int getResY() {
        return resY;
    }

    public int getMaxBounces() {
        return maxBounces;
    }
}
